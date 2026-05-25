package com.ming.stock.service.impl;

import com.google.common.collect.Lists;
import com.ming.stock.mapper.*;
import com.ming.stock.pojo.entity.StockBlockRtInfo;
import com.ming.stock.pojo.entity.StockMarketIndexInfo;
import com.ming.stock.pojo.entity.StockOuterMarketIndexInfo;
import com.ming.stock.pojo.entity.StockRtInfo;
import com.ming.stock.pojo.vo.StockInfoConfig;
import com.ming.stock.service.StockTimerTaskService;
import com.ming.stock.utils.DateTimeUtil;
import com.ming.stock.utils.IdWorker;
import com.ming.stock.utils.ParseType;
import com.ming.stock.utils.ParserStockInfoUtil;
import groovy.lang.Lazy;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author: Ming
 * @Description TODO
 * 获取外部数据的一个类
 */
@Service
@Slf4j
public class StockTimerTaskServiceImpl implements StockTimerTaskService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StockInfoConfig stockInfoConfig;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;
    @Autowired
    private StockBusinessMapper stockBusinessMapper;
    @Autowired
    private ParserStockInfoUtil parserStockInfoUtil;
    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;
    @Autowired
    private StockOuterMarketIndexInfoMapper stockOuterMarketIndexInfoMapper;

    //一定是无状态的
    private HttpEntity<Object> httpEntity;
    /**
     * 获取国内大盘的数据信息
     * 获取url-> 发送请求数据 获得响应 正则表达式解析数据 存入数据库
     */
    @Override
    public void getInnerMarketInfo() {
        //1.采集原始数据
        //1.1组装url地址
        //String url = "http://hq.sinajs.cn/list=sh000001,sz399001";
        String url = stockInfoConfig.getMarketUrl()+String.join(",",stockInfoConfig.getInner());
        //1.2维护请求头 添加防盗链和用户标识
//        HttpHeaders headers = new HttpHeaders();
//        //防盗链
//        headers.add("Referer","https://finance.sina.com.cn/stock/");
//        //用户客户端标识
//        headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36");
//        //维护http请求实体对象
//        HttpEntity httpEntity = new HttpEntity(headers);
        //发送请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        //状态码
        int statusCodeValue =  responseEntity.getStatusCodeValue();
        if (statusCodeValue!=200){
            //当前请求失败
            log.error("当前时间点:{},采集数据失败,http状态码:{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),statusCodeValue);
            //其他操作:发送邮件  企业微信 钉钉给相关运维人员提醒
            return;
        }
        //获取js格式数据
        String jsData = responseEntity.getBody();
        log.info("当前时间点:{},采集原始数据内容:{}",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),jsData);
        //2.Java正则解析原始数据
        //2.1定义正则表达式
        //String reg = "var hq_str_sh000001"="上证指数,3595.8099,3597.9369,3595.1892,3605.5432,3585.9435,0,0,375802719,492038351376,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2025-07-29,11:45:15,00,";
        String reg = "var hq_str_(.+)=\"(.+)\";";
        //2.2表达式编译
        Pattern pattern = Pattern.compile(reg);
        //2.3匹配字符串
        Matcher matcher = pattern.matcher(jsData);
        ArrayList<StockMarketIndexInfo> entities = new ArrayList<>();
        while (matcher.find()){
            //1.获取大盘的编码
            String marketCode = matcher.group(1);
            //2.获取其他信息
            String otherInfo = matcher.group(2);
            //将other字符串以逗号切割 获取大盘的详细信息
            String[] splitArr = otherInfo.split(",");
            //大盘名称
            String marketName = splitArr[0];
            //获取当前大盘的开盘点
            BigDecimal openPoint = new BigDecimal(splitArr[1]);
            //前收盘点
            BigDecimal preClosePoint = new BigDecimal(splitArr[2]);
            //大盘但钱点
            BigDecimal curPoint = new BigDecimal(splitArr[3]);
            //大盘最高点
            BigDecimal maxPoint = new BigDecimal(splitArr[4]);
            //大盘最低点
            BigDecimal minPoint = new BigDecimal(splitArr[5]);
            //获取成交量
            Long tradeAmt = Long.valueOf(splitArr[8]);
            //获取成交金额
            BigDecimal tradeVol = new BigDecimal(splitArr[9]);
            //时间
            Date curTime = DateTimeUtil.getDateTimeWithoutSecond(splitArr[30]+" "+splitArr[31]).toDate();

            //3.解析数据封装entity
            StockMarketIndexInfo info = StockMarketIndexInfo.builder()
                    .id(idWorker.nextId())
                    .marketCode(marketCode)
                    .marketName(marketName)
                    .curPoint(curPoint)
                    .openPoint(openPoint)
                    .preClosePoint(preClosePoint)
                    .maxPoint(maxPoint)
                    .minPoint(minPoint)
                    .tradeVolume(tradeVol)
                    .tradeAmount(tradeAmt)
                    .curTime(curTime)
                    .build();
            entities.add(info);
        }
        log.info("解析数据完毕");
        //4.调用mybatis批量入库
        int count = stockMarketIndexInfoMapper.insertBatch(entities);
        if (count>0){
            //大盘采集数据完毕之后 通知backend工程刷新缓存
            //发送日期对象 接收日期与当前日期的对比 能判断出数据的延迟市场 用于运维的通知处理
            rabbitTemplate.convertAndSend("stockExchange","inner.market",new Date());

            log.info("当前时间:{},插入大盘数据:{}成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),entities);
        }else {
            log.error("当前时间:{},插入大盘数据:{}失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),entities);
        }

    }
    /**
     * 定义获取分钟级股票数据
     */
    @Override
    public void getStockRtIndex() {
        //1.获取所有的个股集合
        List<String> stockIds = stockBusinessMapper.getStockIds();
        //添加大盘业务前缀
        stockIds =  stockIds.stream().map(code -> code.startsWith("6") ? "sh" + code : "sz" + code).collect(Collectors.toList());
        //一次性将所有的集合拼接到url地址中 导致地址过长参数过多
        //String url = stockInfoConfig.getMarketUrl()+String.join(",",stockIds);
        //将有个股编码组成打大集合拆分若干个小集合40---->15 15 10
        Lists.partition(stockIds,15).forEach(codes->{
            //1.1分批次采集
            String url = stockInfoConfig.getMarketUrl()+String.join(",",codes);
            //1.2维护请求头 添加防盗链和用户标识
//            HttpHeaders headers = new HttpHeaders();
//            //防盗链
//            headers.add("Referer","https://finance.sina.com.cn/stock/");
//            //用户客户端标识
//            headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36");
//            //维护http请求实体对象
//            HttpEntity httpEntity = new HttpEntity(headers);
            //发送请求
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
            //状态码
            int statusCodeValue =  responseEntity.getStatusCodeValue();
            if (statusCodeValue!=200){
                //当前请求失败
                log.error("当前时间点:{},采集数据失败,http状态码:{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),statusCodeValue);
                //其他操作:发送邮件  企业微信 钉钉给相关运维人员提醒
                return;
            }
            //获取js格式数据
            String jsData = responseEntity.getBody();
            List<StockRtInfo> list = parserStockInfoUtil.parser4StockOrMarketInfo(jsData, ParseType.ASHARE);
            log.info("采集个股数据:{}",list);
            //批量插入
            int count = stockRtInfoMapper.insertBatch(list);
            if (count>0){
                log.info("当前时间:{}, 插入个股数据:{}成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
            }else {
                log.error("当前时间:{},插入个股数据:{}失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
            }

        });
    }
    //https://vip.stock.finance.sina.com.cn/q/view/newSinaHy.php
    //获取板块实时数据
    @Override
    public void getStockSectorRtIndex() {
        //发送板块数据请求
        String result = restTemplate.getForObject(stockInfoConfig.getBlockUrl(),String.class);
        //响应结果转化板块集合数据
        List<StockBlockRtInfo> infos = parserStockInfoUtil.parse4StockBlock(result);
        log.info("板块数据量:{}",infos.size());
        //数据分片保存到数据库下
        Lists.partition(infos,20).forEach(list->{
            //20各一组 批量插入
            stockBlockRtInfoMapper.insertBatch(list);
        });
    }

    @Override
    public void getOuterStockInfo() {
        //拼装url
        String url=stockInfoConfig.getMarketUrl()+String.join(",",stockInfoConfig.getOuter());
        System.out.println(url);
        //获取数据
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
       String jsData=exchange.getBody();
        //数据解析
      List<StockOuterMarketIndexInfo>infos=parserStockInfoUtil.parser4StockOrMarketInfo(jsData,ParseType.OUTER);
        //数据更新
        Lists.partition(infos,15).forEach(list->{
            for (StockOuterMarketIndexInfo info : list) {
                info.setId(idWorker.nextId());
            }
           int i= stockOuterMarketIndexInfoMapper.insertInfo(list);
           if (i!=0){
               log.info("外股数据采集成功! 一共有"+i+"条数据背录入!");
           }
        });
    }

    /*
    * bean的生命周期的初始化回调方法
    * */
    @PostConstruct
    public void initData(){
        //1.2维护请求头 添加防盗链和用户标识
        HttpHeaders headers = new HttpHeaders();
        //防盗链
        headers.add("Referer","https://finance.sina.com.cn/stock/");
        //用户客户端标识
        headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36");
        //维护http请求实体对象
        httpEntity = new HttpEntity(headers);
    }
}

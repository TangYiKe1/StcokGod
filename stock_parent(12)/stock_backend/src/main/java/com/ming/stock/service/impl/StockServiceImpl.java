package com.ming.stock.service.impl;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ming.stock.mapper.*;
import com.ming.stock.pojo.domain.*;
import com.ming.stock.pojo.entity.StockBusiness;
import com.ming.stock.pojo.entity.StockOuterMarketIndexInfo;
import com.ming.stock.pojo.entity.StockRtInfo;
import com.ming.stock.pojo.my.GetChangeLim10;
import com.ming.stock.pojo.my.MyStockInfo;
import com.ming.stock.pojo.my.PersonInfo;
import com.ming.stock.pojo.my.WeekInfoDomin;
import com.ming.stock.pojo.vo.StockInfoConfig;
import com.ming.stock.service.StockService;
import com.ming.stock.utils.DateTimeUtil;
import com.ming.stock.vo.resp.PageResult;
import com.ming.stock.vo.resp.R;
import com.ming.stock.vo.resp.ResponseCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Ming
 * @Description TODO
 */
@Service
@Slf4j
public class StockServiceImpl implements StockService {
    @Autowired
    private StockInfoConfig stockInfoConfig;
    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;
    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;
    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;
    @Autowired
    private Cache<String,Object> caffeineCache;
@Autowired
private StockOuterMarketIndexInfoMapper stockOuterMarketIndexInfoMapper;
@Autowired
private StockBusinessMapper stockBusinessMapper;
    /**
     * 获取国内大盘最新数据
     * @return
     */ 
    @Override
    public R<List<InnerMarketDomain>> getInnerMarketInfo() {
        R<List<InnerMarketDomain>> result = (R<List<InnerMarketDomain>>) caffeineCache.get("innerMarketKey", key->{
            //1.获取股票最新交易时间点(精确到分钟 秒 毫秒置为0)
            //因为没有最新数据 所以curDate mCodes 等后续完成股票采集就可以了 再将其删除
            Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
            curDate = DateTime.parse("2022-12-28 9:31:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
            //2.获取大盘编码
            List<String> mCodes = stockInfoConfig.getInner();
            //3.调用mapper查询数据
            List<InnerMarketDomain> data =  stockMarketIndexInfoMapper.getMarketInfo(curDate,mCodes);
            //4.封装数据并响应
            return R.ok(data);
        });
        return result;

    }
    /**
     * 获取板块最新数据
     * @return
     */
    @Override
    public R<List<StockBlockDomain>> sectorAllLimit() {
        //获取股票最新交易时间点
        Date lastDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //mock数据 后续删除
        lastDate = DateTime.parse("2022-12-21 09:30:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //调用mapper接口获取数据
        List<StockBlockDomain> infos = stockBlockRtInfoMapper.sectorAllLimit(lastDate);
        //组装数据
        if (CollectionUtils.isEmpty(infos)){
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        return R.ok(infos);
    }
    /**
     * 分页查询股票最新数据 并且按照涨幅排序查询
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public R<PageResult<StockUpdownDomain>> getStockPageInfo(Integer page, Integer pageSize) {
        //1.获取股票最新交易时间点(精确到分钟 秒 毫秒置为0)
        //因为没有最新数据 所以curDate mCodes 等后续完成股票采集就可以了 再将其删除
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        curDate = DateTime.parse("2022-12-30 09:32:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //2.设置PageHelper分页参数
        PageHelper.startPage(page,pageSize);
        //3.调用mapper查询
        List<StockUpdownDomain> pageData =  stockRtInfoMapper.getStockPageInfoByTime(curDate);
        //4.组装PageInfo对象 获取分页的具体信息
        PageInfo<StockUpdownDomain> pageInfo = new PageInfo<>(pageData);
        PageResult<StockUpdownDomain> pageResult = new PageResult<>(pageInfo);
        //5.响应数据
        return R.ok(pageResult);
    }
    /**
     * 统计沪深两市个股最新交易数据 并且按照涨幅降序排序查前四条
     * @return
     */
    @Override
    public R<List<StockUpdownDomain>> getTopStockCreate() {
        //1.获取股票最新交易时间点(精确到分钟 秒 毫秒置为0)
        //因为没有最新数据 所以curDate mCodes 等后续完成股票采集就可以了 再将其删除
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        curDate = DateTime.parse("2022-12-30 09:32:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //调用mapper
        List<StockUpdownDomain> topStocks = stockRtInfoMapper.findTopStockIncreate(curDate);
        //返回数据
        return R.ok(topStocks);
    }
    /**
     * 统计最新股票交易日每分钟的涨跌停的股票数量
     * @return
     */
    @Override
    public R<Map<String, List>> getStockUpdownCount() {
        //1.获取股票最新交易时间点(精确到分钟 秒 毫秒置为0)
        //因为没有最新数据 所以curDate mCodes 等后续完成股票采集就可以了 再将其删除
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        curDateTime = DateTime.parse("2023-01-06 14:55:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endDate = curDateTime.toDate();
        //2.获取最新交易时间点 对应的开盘时间点
        Date startDate = DateTimeUtil.getOpenDate(curDateTime).toDate();
        //3.统计涨停数据   约定mapper中flag入参: 1-->涨停数据  0-->跌停
        List<Map> upList = stockRtInfoMapper.getStockUpdownCount(startDate,endDate,1);
        //4.统计跌停数据
        List<Map> downList = stockRtInfoMapper.getStockUpdownCount(startDate,endDate,0);
        //5.组装数据
        HashMap<String,List> info = new HashMap<>();
        info.put("upList",upList);
        info.put("downList",downList);
        return R.ok(info);
    }

    @Override
    public void exportStockUpDownInfo(Integer page, Integer pageSize, HttpServletResponse response) {
        //1.获取分页数据
        R<PageResult<StockUpdownDomain>> r = this.getStockPageInfo(page,pageSize);
        List<StockUpdownDomain> rows = r.getData().getRows();
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");

        try {
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("stockRt", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), StockUpdownDomain.class)
                    .sheet("股票信息").doWrite(rows);
        } catch (IOException e) {
            log.error("当前页码:{},每页大小:{},当前时间:{},异常信息:{}",
                    page,pageSize,DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),e.getMessage());
            //通知前端异常 稍后重试
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            R<Object> error = R.error(ResponseCode.ERROR);
            try {
                String jsonData = new ObjectMapper().writeValueAsString(error);
                response.getWriter().write(jsonData);
            } catch (IOException ex) {
                log.error("exportStockUpDownInfo:响应错误信息失败:时间:{}",
                        page,pageSize,DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
            }
        }
    }
    /**
     * 统计国内A大盘T日和T-1日成交量对比功能
     * @return
     */
    @Override
    public R<Map<String, List>> getComparedStockTradeAmt() {
        //1.获取T日和T-1日的开始时间和结束时间
        //1.1获取最新股票交易日的日期范围 T日的时间范围
        DateTime tEndDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());//T日的截止时间
        tEndDateTime = DateTime.parse("2023-1-3 14:40:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        //转化称Java中Date
        Date tEndDate = tEndDateTime.toDate();
        //开盘时间
        Date tStartDate = DateTimeUtil.getOpenDate(tEndDateTime).toDate();
        //1.2获取T-1日的时间范围
        DateTime previousTradingDay = DateTimeUtil.getPreviousTradingDay(tEndDateTime);
        previousTradingDay = DateTime.parse("2023-1-2 14:40:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        //转化称Java中Date
        Date preTEndDate = previousTradingDay.toDate();
        //开盘时间
        Date tPreStartDate = DateTimeUtil.getOpenDate(previousTradingDay).toDate();
        //2.调用mapper查询
        //2.1统计T日交易数据  第三个参数大盘编码集合
        List<Map> tData = stockMarketIndexInfoMapper.getSumAmtInfo(tStartDate,tEndDate,stockInfoConfig.getInner());
        //2.2统计T-1日交易数据
        List<Map> preTData = stockMarketIndexInfoMapper.getSumAmtInfo(tPreStartDate,preTEndDate,stockInfoConfig.getInner());
        //3.组装数据
        HashMap<String,List> info = new HashMap<>();
        info.put("amtList",tData);
        info.put("yesAmtList",preTData);
        //4.响应数据
        return R.ok(info);
    }
    /**
     * 统计最新交易时间点下股票 在各个涨幅区间的数量
     * @return
     */
    @Override
    public R<Map> getIncreaseRangeInfo() {
        //1.获取当前最新的股票交易时间点
        DateTime cuDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());//t日的截止时间点
        cuDateTime = DateTime.parse("2023-07-07 14:55:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date curDate = cuDateTime.toDate();

        //2.调用Mapper数据
        List<Map> infos = stockRtInfoMapper.getIncreaseRangeInfo(curDate);
        //获取有序的涨幅区间标题集合
        List<String> upDownRange = stockInfoConfig.getUpDownRange();
        //方式一将顺序的涨幅区间内的每个元素转化成map对象即可
        /*List<Map> allInfos = new ArrayList<>();
        Map tmp = null;
        for (String title:upDownRange){
            for (Map info : infos){
                if (info.containsValue(title)){
                    tmp = info;
                    break;
                }
            }
            if (tmp == null){
                //则不存在
                tmp = new HashMap();
                tmp.put("count",0);
                tmp.put("title",title);
            }
            allInfos.add(tmp);
        }*/
        //方式二:stream遍历循环
        List<Map> allInfos = upDownRange.stream().map(title -> {
            Optional<Map> result = infos.stream().filter(map ->
                map.containsValue(title)).findFirst();
            //判断是否符合过滤的条件的元素
            if (result.isPresent()){
                return result.get();
            }else {
                HashMap<String,Object> tmp = new HashMap<>();
                tmp.put("count",0);
                tmp.put("title",title);
                return tmp;
            }
        }).collect(Collectors.toList());
        //3.组装数据
        HashMap<String,Object> data = new HashMap<>();
        //获取指定日期的字符串
        data.put("time",cuDateTime.toString("yyyy-MM-dd HH:mm:ss"));
        data.put("infos",allInfos);
        //4.返回数据
        return R.ok(data);
    }
    /**
     * 获取指定股票T日分时数据
     * @param stockCode
     * @return
     */
    @Override
    public R<List<Stock4MinuteDomain>> getStockScreenTimeSharing(String stockCode) {
        //1.获取T日最新股票交易时间点 endTime
        DateTime endDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        endDateTime=DateTime.parse("2022-12-30 14:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endDate = endDateTime.toDate();
        Date openDate = DateTimeUtil.getOpenDate(endDateTime).toDate();
        //2.查询
        List<Stock4MinuteDomain> data = stockRtInfoMapper.getStockScreenTimeSharing(openDate,endDate,stockCode);
        //3.返回
        return R.ok(data);
    }
    /**
     * 个股日K数据查询
     * @param stockCode
     * @return
     */
    @Override
    public R<List<Stock4EvrDayDomain>> getStockScreenDKLine(String stockCode) {
        //1.获取T日最新股票交易时间点 endTime
        DateTime endDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        endDateTime=DateTime.parse("2023-01-20 15:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endDate = endDateTime.toDate();
        //获取开始时间
        DateTime startDateTime = endDateTime.minusDays(10);
        startDateTime = DateTime.parse("2023-01-01 09:30:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date startDate = startDateTime.toDate();
        //2.调用mapper获取指定日期范围内的日K数据
        List<Stock4EvrDayDomain> dkLineData = stockRtInfoMapper.getStockScreenDKLine(startDate,endDate,stockCode);
        //3.方案二:分步实现
        List<Date> mxTimes = stockRtInfoMapper.getMxTime4EvryDay(stockCode,startDate,endDate);
        List<Stock4EvrDayDomain> infos = stockRtInfoMapper.getStock4DkLine2(stockCode,mxTimes);
        if (CollectionUtils.isEmpty(infos)){
            return R.error(ResponseCode.NO_RESPONSE_DATA);
        }
        //3.返回
        return R.ok(infos);

    }

    @Override
    public R<List<MyStockInfo>> getPre4() {
      List<StockOuterMarketIndexInfo> data=stockOuterMarketIndexInfoMapper.getPre4();
      List<MyStockInfo> ans=new ArrayList<>();
        for (StockOuterMarketIndexInfo datum : data) {
            MyStockInfo myStockInfo = MyStockInfo.builder()
                    .name(datum.getMarketName())
                    .curPoint(datum.getCurPoint())
                    .upDown(datum.getUpdown())
                    .rose(datum.getRose())
                    .curTime(datum.getCurTime())
                    .build();
            ans.add(myStockInfo);
        }
        return R.ok(ans);
    }

    @Override
    public R<Map> getPersonInfoStock(String code) {
        StockBusiness info=stockBusinessMapper.getPersonInfoStock(code);
        Map ans=new HashMap();
        ans.put("code",info.getStockCode());
        ans.put("trade",info.getBlockName());
        ans.put("business",info.getBusiness());
        ans.put("name",info.getStockName());
        return R.ok(ans);
    }

    @Override
    public R<PersonInfo> getPersonInfo(String code) {
      PersonInfo pr=stockRtInfoMapper.getPersonInfo(code);
      return R.ok(pr);
    }

    @Override
    public R<List<GetChangeLim10>> getCahngeLim10(String code) {
        List<GetChangeLim10> info=stockRtInfoMapper.getCahngeLim10(code);
        return R.ok(info);
    }

    @Override
    public R<List<Map>> selectNotClaer(String resInfo) {
      List<StockRtInfo> data= stockRtInfoMapper.selectNotClaer(resInfo);
        List<Map> ans = data.stream().map(info->{
            Map<String,String> map=new HashMap<>();
            map.put("name", info.getStockName());
            map.put("code", info.getStockCode());
            return map;
        }).collect(Collectors.toList());

        return R.ok(ans);
    }

    @Override
    public R<WeekInfoDomin> WeekInfoSelect(String code) {
        DateTime date = DateTime.parse("2022-12-30 16:44:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));

        // 获取该日期所在周的周一开盘时间
        DateTime preTime = date.withDayOfWeek(DateTimeConstants.MONDAY)
                .withHourOfDay(9).withMinuteOfHour(30).withSecondOfMinute(0);

        // 获取该日期所在周的周五收盘时间
        DateTime lastTime = date.withDayOfWeek(DateTimeConstants.FRIDAY)
                .withHourOfDay(15).withMinuteOfHour(0).withSecondOfMinute(0);
      //2022-12-30 09:32:00
        Date startTime= DateTime.parse("2023-07-07 09:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        //2022-12-30 09:56:00
Date endTime=DateTime.parse("2025-09-05 10:56:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        WeekInfoDomin infos = stockRtInfoMapper.WeekInfoSelect(code, startTime, endTime);
        return R.ok(infos);
    }

}

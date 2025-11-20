package com.ming.stock.service;

import com.ming.stock.pojo.domain.*;
import com.ming.stock.pojo.my.GetChangeLim10;
import com.ming.stock.pojo.my.MyStockInfo;
import com.ming.stock.pojo.my.PersonInfo;
import com.ming.stock.pojo.my.WeekInfoDomin;
import com.ming.stock.vo.resp.PageResult;
import com.ming.stock.vo.resp.R;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hpsf.Decimal;

import java.util.List;
import java.util.Map;

/**
 * @Author: Ming
 * @Description TODO
 */
public interface StockService {




    /**
     * 获取国内大盘最新数据
     * @return
     */
    R<List<InnerMarketDomain>> getInnerMarketInfo();
    /**
     * 获取板块最新数据
     * @return
     */
    R<List<StockBlockDomain>> sectorAllLimit();
    /**
     * 分页查询股票最新数据 并且按照涨幅排序查询
     * @param page 当前页
     * @param pageSize 每页大小
     * @return
     */
    R<PageResult<StockUpdownDomain>> getStockPageInfo(Integer page, Integer pageSize);
    /**
     * 统计沪深两市个股最新交易数据 并且按照涨幅降序排序查前四条
     * @return
     */
    R<List<StockUpdownDomain>> getTopStockCreate();
    /**
     * 统计最新股票交易日每分钟的涨跌停的股票数量
     * @return
     */
    R<Map<String, List>> getStockUpdownCount();

    /**
     * 将指定页的股票数据导出到excel表中
     * @param page
     * @param pageSize
     * @param response
     */
    void exportStockUpDownInfo(Integer page, Integer pageSize, HttpServletResponse response);
    /**
     * 统计国内A大盘T日和T-1日成交量对比功能
     * @return
     */
    R<Map<String, List>> getComparedStockTradeAmt();
    /**
     * 统计最新交易时间点下股票 在各个涨幅区间的数量
     * @return
     */
    R<Map> getIncreaseRangeInfo();
    /**
     * 获取指定股票T日分时数据
     * @param stockCode
     * @return
     */
    R<List<Stock4MinuteDomain>> getStockScreenTimeSharing(String stockCode);
    /**
     * 个股日K数据查询
     * @param stockCode
     * @return
     */
    R<List<Stock4EvrDayDomain>> getStockScreenDKLine(String stockCode);

    R<List<MyStockInfo>> getPre4();

    R<Map> getPersonInfoStock(String code);

    R<PersonInfo> getPersonInfo(String code);

    R<List<GetChangeLim10>> getCahngeLim10(String code);

    R<List<Map>> selectNotClaer(String resInfo);

    R<WeekInfoDomin> WeekInfoSelect(String code);
}

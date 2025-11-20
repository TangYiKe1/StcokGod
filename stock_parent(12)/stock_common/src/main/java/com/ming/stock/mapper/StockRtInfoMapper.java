package com.ming.stock.mapper;

import com.ming.stock.pojo.domain.Stock4EvrDayDomain;
import com.ming.stock.pojo.domain.Stock4MinuteDomain;
import com.ming.stock.pojo.domain.StockUpdownDomain;
import com.ming.stock.pojo.entity.StockRtInfo;
import com.ming.stock.pojo.my.GetChangeLim10;
import com.ming.stock.pojo.my.PersonInfo;
import com.ming.stock.pojo.my.WeekInfoDomin;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author 11519
* @description 针对表【stock_rt_info(个股详情信息表)】的数据库操作Mapper
* @createDate 2025-07-19 09:35:05
* @Entity com.ming.stock.pojo.entity.StockRtInfo
*/
public interface StockRtInfoMapper {
    WeekInfoDomin WeekInfoSelect(@Param("code") String code,
                                 @Param("preTime") Date preTime,
                                 @Param("lastTime") Date lastTime);

    int deleteByPrimaryKey(Long id);

    int insert(StockRtInfo record);

    int insertSelective(StockRtInfo record);

    StockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockRtInfo record);

    int updateByPrimaryKey(StockRtInfo record);

    /**
     * 查询指定时间点下的股票数据 并且按照涨幅降序排序
     * @param curDate  日期
     * @return
     */
    List<StockUpdownDomain> getStockPageInfoByTime(@Param("curDate") Date curDate);
    /**
     * 统计沪深两市个股最新交易数据 并且按照涨幅降序排序查前四条
     * @return
     */
    List<StockUpdownDomain> findTopStockIncreate(@Param("curDate") Date curDate);

    /**
     * 统计最新股票交易日每分钟的涨跌停的股票数量
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param flag    约定:1->涨停  0->跌停
     * @return
     */
    List<Map> getStockUpdownCount(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("flag") int flag);
    /**
     * 统计最新交易时间点下股票 在各个涨幅区间内股票的个数
     * @return
     */
    List<Map> getIncreaseRangeInfo(@Param("curDate") Date curDate);

    /**
     * 查询分时数据
     * @param openDate 开盘时间
     * @param endDate  截止时间
     * @param stockCode 股票编码
     * @return
     */
    List<Stock4MinuteDomain> getStockScreenTimeSharing(
            @Param("openDate") Date openDate, @Param("endDate") Date endDate, @Param("stockCode") String stockCode);

    /**
     * 个股日K数据查询
     * @param startDate 开盘时间
     * @param endDate  截止时间
     * @param stockCode 股票编码
     * @return
     */
    List<Stock4EvrDayDomain> getStockScreenDKLine(
            @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("stockCode") String stockCode);

    /**
     * 批量插入个股数据
     * @param list
     * @return
     */
    int insertBatch(@Param("list") List<StockRtInfo> list);

    /**
     * 获取指定日期范围内的收盘日期
     * @param stockCode  股票编码
     * @param startDate 起始时间
     * @param endDate  结束时间
     * @return
     */
    List<Date> getMxTime4EvryDay(
            @Param("stockCode") String stockCode, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 获取指定股票在指定日期下的数据
     * @param stockCode
     * @param mxTimes
     * @return
     */

    List<Stock4EvrDayDomain> getStock4DkLine2(
            @Param("stockCode") String stockCode, @Param("mxTimes") List<Date> mxTimes);

    PersonInfo getPersonInfo(@Param("code") String code);

    List<GetChangeLim10> getCahngeLim10(@Param("code") String code);

    List<StockRtInfo> selectNotClaer(@Param("resInfo") String resInfo);
}

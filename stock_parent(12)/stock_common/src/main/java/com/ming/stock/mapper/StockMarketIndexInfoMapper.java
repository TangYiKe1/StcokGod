package com.ming.stock.mapper;

import com.ming.stock.pojo.domain.InnerMarketDomain;
import com.ming.stock.pojo.entity.StockMarketIndexInfo;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author 11519
* @description 针对表【stock_market_index_info(国内大盘数据详情表)】的数据库操作Mapper
* @createDate 2025-07-19 09:35:05
* @Entity com.ming.stock.pojo.entity.StockMarketIndexInfo
*/
public interface StockMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockMarketIndexInfo record);

    int insertSelective(StockMarketIndexInfo record);

    StockMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockMarketIndexInfo record);

    int updateByPrimaryKey(StockMarketIndexInfo record);

    /**
     * 根据指定时间查询指定大盘编码对应的数据
     * @param curDate 指定的时间点
     * @param mCodes  大盘编码集合
     * @return
     */
    List<InnerMarketDomain> getMarketInfo(@Param("curDate") Date curDate, @Param("mCodes") List<String> mCodes);

    /**
     * 统计指定日期范围内 指定大盘每分钟的成交量流水信息
     * @param openDate 起始时间
     * @param endDate  截止时间
     * @param marketCodes 大盘编码集合
     * @return
     */
    List<Map> getSumAmtInfo(
            @Param("openDate") Date openDate,
            @Param("endDate") Date endDate,
            @Param("marketCodes") List<String> marketCodes);

    /**
     * 批量插入大盘数据
     * @param entities  大盘实体对象集合
     * @return
     */
    int insertBatch(@Param("entities") ArrayList<StockMarketIndexInfo> entities);
}

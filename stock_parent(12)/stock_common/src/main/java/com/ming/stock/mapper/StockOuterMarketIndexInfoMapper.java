package com.ming.stock.mapper;

import com.ming.stock.pojo.entity.StockOuterMarketIndexInfo;
import com.ming.stock.pojo.my.MyStockInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 11519
* @description 针对表【stock_outer_market_index_info(外盘详情信息表)】的数据库操作Mapper
* @createDate 2025-07-19 09:35:05
* @Entity com.ming.stock.pojo.entity.StockOuterMarketIndexInfo
*/
public interface StockOuterMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockOuterMarketIndexInfo record);

    int insertSelective(StockOuterMarketIndexInfo record);

    StockOuterMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockOuterMarketIndexInfo record);

    int updateByPrimaryKey(StockOuterMarketIndexInfo record);


    List<StockOuterMarketIndexInfo> getPre4();

    int insertInfo(@Param("list") List<StockOuterMarketIndexInfo> list);
}

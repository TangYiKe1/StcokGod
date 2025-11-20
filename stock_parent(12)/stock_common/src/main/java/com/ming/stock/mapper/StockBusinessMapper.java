package com.ming.stock.mapper;

import com.ming.stock.pojo.entity.StockBusiness;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* @author 11519
* @description 针对表【stock_business(主营业务表)】的数据库操作Mapper
* @createDate 2025-07-19 09:35:05
* @Entity com.ming.stock.pojo.entity.StockBusiness
*/
public interface StockBusinessMapper {

    int deleteByPrimaryKey(String id);

    int insert(StockBusiness record);

    int insertSelective(StockBusiness record);

    StockBusiness selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(StockBusiness record);

    int updateByPrimaryKey(StockBusiness record);
    /*
    * 获取所有的A股编码集合
    * */
    List<String> getStockIds();

    StockBusiness getPersonInfoStock(@Param("code") String code);
}

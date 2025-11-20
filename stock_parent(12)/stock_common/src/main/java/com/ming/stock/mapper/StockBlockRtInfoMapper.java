package com.ming.stock.mapper;

import com.ming.stock.pojo.domain.StockBlockDomain;
import com.ming.stock.pojo.entity.StockBlockRtInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* @author 11519
* @description 针对表【stock_block_rt_info(股票板块详情信息表)】的数据库操作Mapper
* @createDate 2025-07-19 09:35:05
* @Entity com.ming.stock.pojo.entity.StockBlockRtInfo
*/
public interface StockBlockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockBlockRtInfo record);

    int insertSelective(StockBlockRtInfo record);

    StockBlockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockBlockRtInfo record);

    int updateByPrimaryKey(StockBlockRtInfo record);
    /**
     * 获取板块最新数据
     * @return
     */
    List<StockBlockDomain> sectorAllLimit(@Param("lastDate") Date lastDate);

    /**
     * 板块信息批量插入
     * @param list
     */
    void insertBatch(List<StockBlockRtInfo> list);
}

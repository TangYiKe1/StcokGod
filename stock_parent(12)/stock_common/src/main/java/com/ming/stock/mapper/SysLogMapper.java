package com.ming.stock.mapper;

import com.ming.stock.pojo.entity.SysLog;

/**
* @author 11519
* @description 针对表【sys_log(系统日志)】的数据库操作Mapper
* @createDate 2025-07-19 09:35:05
* @Entity com.ming.stock.pojo.entity.SysLog
*/
public interface SysLogMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKey(SysLog record);

}

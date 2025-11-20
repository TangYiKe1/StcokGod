package com.ming.stock.mapper;

import com.ming.stock.pojo.entity.SysUserRole;

/**
* @author 11519
* @description 针对表【sys_user_role(用户角色表)】的数据库操作Mapper
* @createDate 2025-07-19 09:35:05
* @Entity com.ming.stock.pojo.entity.SysUserRole
*/
public interface SysUserRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    SysUserRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUserRole record);

    int updateByPrimaryKey(SysUserRole record);

}

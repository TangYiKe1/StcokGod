package com.ming.stock.mapper;

import com.ming.stock.pojo.entity.SysPermission;
import com.ming.stock.pojo.my.PermissionVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 11519
* @description 针对表【sys_permission(权限表（菜单）)】的数据库操作Mapper
* @createDate 2025-07-19 09:35:05
* @Entity com.ming.stock.pojo.entity.SysPermission
*/
public interface SysPermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

    List<SysPermission> findall();

    Integer addPermission(@Param("permissionVo") PermissionVo permissionVo);

    int deleteByidPermiison(@Param("id") String id);
}

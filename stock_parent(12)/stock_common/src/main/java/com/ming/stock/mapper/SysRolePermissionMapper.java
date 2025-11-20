package com.ming.stock.mapper;

import com.ming.stock.pojo.entity.SysRolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 11519
* @description 针对表【sys_role_permission(角色权限表)】的数据库操作Mapper
* @createDate 2025-07-19 09:35:05
* @Entity com.ming.stock.pojo.entity.SysRolePermission
*/
public interface SysRolePermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRolePermission record);

    int insertSelective(SysRolePermission record);

    SysRolePermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRolePermission record);

    int updateByPrimaryKey(SysRolePermission record);
    // 删除角色下的权限
    int deleteByRoleId(@Param("roleId") Long roleId);
    int add(@Param("roleId") Long roleId, @Param("id") Long id, @Param("permissionIds") List<Long> permissionIds);

    void addinfo(@Param("roleid") long roleid, @Param("ids") List<Long> ids);
}

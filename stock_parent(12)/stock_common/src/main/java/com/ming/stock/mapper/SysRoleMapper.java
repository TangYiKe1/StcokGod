package com.ming.stock.mapper;

import com.ming.stock.pojo.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author 11519
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2025-07-19 09:35:05
* @Entity com.ming.stock.pojo.entity.SysRole
*/
public interface SysRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    List<SysRole> getInfoByUserId(@Param("id") String id);

    List<SysRole> getAllinfos();

    List<SysRole> getAllinfosByPages();

    Long getIdByName(@Param("name") String name);

    Integer deleteRoleById(@Param("id") Integer id);

    List<String> getAllroles();

    List<Map<String, Object>> selectAllPermissions();

    void addUser(@Param("roleid") long roleid, @Param("name") String name, @Param("des") String des);
}

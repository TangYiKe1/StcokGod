package com.ming.stock.mapper;

import com.ming.stock.pojo.entity.SysUser;
import com.ming.stock.pojo.my.MyUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 11519
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2025-07-19 09:35:05
* @Entity com.ming.stock.pojo.entity.SysUser
*/
public interface SysUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    /**
     * 根据用户名称查询用户信息
     * @param userName
     * @return
     */
    SysUser findByUsernameSysUser(@Param("userName") String userName);

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    SysUser findByUserName(@Param("username") String username);

    /**
     * 查询所有用户信息
     * @return
     */
    List<SysUser> findAll();

    List<SysUser> getAllUserInfo(@Param("username") String username, @Param("nickName") String nickName, @Param("startTime") String startTime, @Param("endTime") String endTime);

    int addUser(@Param("user") MyUser user);

    void DeleteAllroleAndUserInfoByUserId(@Param("userId") String userId);

    int addDateInfo(@Param("userId") String userId, @Param("roleIds") List<String> roleIds);

    int deleteByIds(@Param("ids") List<Long> ids);

    SysUser GetById(@Param("id") String id);

    int updateById(@Param("sysUser") SysUser sysUser);

    int deleteByidRoel(@Param("id") String id);

    int updateStatusById(@Param("id") String id, @Param("status") String status);

}

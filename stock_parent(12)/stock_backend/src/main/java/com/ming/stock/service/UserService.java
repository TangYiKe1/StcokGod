package com.ming.stock.service;

import com.ming.stock.pojo.domain.RolesDomin;
import com.ming.stock.pojo.entity.SysPermission;
import com.ming.stock.pojo.entity.SysRole;
import com.ming.stock.pojo.entity.SysUser;
import com.ming.stock.vo.req.LoginReqVo;
import com.ming.stock.pojo.my.MyUser;
import com.ming.stock.pojo.my.PermissionVo;
import com.ming.stock.vo.req.my.UpDateInfos;
import com.ming.stock.vo.req.my.UserRes;
import com.ming.stock.vo.resp.LoginRespVo;
import com.ming.stock.vo.resp.PageResult;
import com.ming.stock.vo.resp.R;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Ming
 * @Description 定义用户服务接口
 */
public interface UserService {
    /**
     * 根据用户名称查询用户信息
     * @param userName  用户名称
     * @return
     */
    SysUser getUserByUserName(String userName);

    /**
     * 用户登录功能
     * @param vo
     * @return
     */
    R<LoginRespVo> login(LoginReqVo vo);

    /**
     * 生成验证码功能
     * @return
     */
    R<Map> getCaptCode();


    R<PageResult<SysUser>> getAllUserInfo(UserRes userRes);

    R addUser(MyUser user);

    R<Map<String, Object>> getInfoByUserId(String id);


    R upDateroles(UpDateInfos infos);

    R deleteByid(List<Long> ids);

    R<SysUser> getById(String id);

    R updateinfosById(SysUser sysUser);

    R<PageResult<SysRole>> getAllinfosByPages(Integer num, Integer size);

    R deleteByidRoel(String id);

    R<List<SysPermission>> selectAllPermission();

    R updateRoleById(HashMap<String, Object> resInfo);

    R deleteRoleById1(Integer id);

    R updateStatusById(String id, String status);

    R addPermission(PermissionVo permissionVo);

    R deleteByidPermiison(String id);

    R<List<String>> getAllroles();

    List<RolesDomin> getPermissionTree();

    R addRoles(HashMap<String, Object> infos);

}

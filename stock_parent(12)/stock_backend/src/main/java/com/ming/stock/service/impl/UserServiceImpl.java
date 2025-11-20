package com.ming.stock.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ming.stock.constant.StockConstant;
import com.ming.stock.mapper.*;
import com.ming.stock.pojo.domain.RolesDomin;
import com.ming.stock.pojo.entity.SysPermission;
import com.ming.stock.pojo.entity.SysRole;
import com.ming.stock.pojo.entity.SysUser;
import com.ming.stock.service.UserService;
import com.ming.stock.utils.IdWorker;
import com.ming.stock.vo.req.LoginReqVo;
import com.ming.stock.pojo.my.MyUser;
import com.ming.stock.pojo.my.PermissionVo;
import com.ming.stock.vo.req.my.UpDateInfos;
import com.ming.stock.vo.req.my.UserRes;
import com.ming.stock.vo.resp.LoginRespVo;
import com.ming.stock.vo.resp.PageResult;
import com.ming.stock.vo.resp.R;
import com.ming.stock.vo.resp.ResponseCode;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @Author: Ming
 * @Description 定义用户服务实现
 */
@Service
    @Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;
    /**
     * 根据用户名称查询用户信息
     * @param userName  用户名称
     * @return
     */
    @Override
    public SysUser getUserByUserName(String userName) {
        SysUser user = sysUserMapper.findByUsernameSysUser(userName);
        return user;
    }

    /**
     * 用户登录功能
     * @param vo
     * @return
     */
    @Override
    public R<LoginRespVo> login(LoginReqVo vo) {
        //判断参数是否合法
        if (vo==null
                || StringUtils.isBlank(vo.getUsername())
                ||StringUtils.isBlank(vo.getPassword())
                ||StringUtils.isBlank(vo.getCode())){
            return R.error(ResponseCode.DATA_ERROR);
        }
        //判断redis保存的验证码与输入的验证码是否相同(比较的时候忽略大小写)
        String redisCode = (String) redisTemplate.opsForValue().get(StockConstant.CHECK_PREFIX+vo.getSessionId());
        if (StringUtils.isBlank(redisCode)){
            //验证码过期
            return R.error(ResponseCode.CHECK_CODE_TIMEOUT);
        }
        if (!redisCode.equalsIgnoreCase(vo.getCode())){
            //验证码错误
            return R.error(ResponseCode.CHECK_CODE_ERROR);
        }
        //根据用户名去数据库中查询用户信息 获取密码加密后的密文
        SysUser user = sysUserMapper.findByUserName(vo.getUsername());
        //判断用户是否存在
        if (user == null){
            //用户不存在
            return R.error(ResponseCode.ACCOUNT_NOT_EXISTS);
        }
        //调用密码匹配器匹配输入明文密码和数据密文密码
        if (!passwordEncoder.matches(vo.getPassword(), user.getPassword())){
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }
        //响应
        LoginRespVo respVo = new LoginRespVo();
//        respVo.setUsername(user.getUsername());
//        respVo.setNickName(user.getNickName());
//        respVo.setPhone(user.getPhone());
//        respVo.setId(user.getId());
        BeanUtils.copyProperties(user,respVo);
        return R.ok(respVo);
    }

    /**
     * 生成验证码功能
     * @return
     */
    @Override
    public R<Map> getCaptCode() {
        //1.生成图片验证码 参数分别是:图片的宽 图片的高 图片验证码的长度 干扰线数量
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(250, 40, 4, 5);
        //设置背景颜色
        captcha.setBackground(Color.LIGHT_GRAY);
        //获取图片中的验证码 默认生成验证码包含文字数字 长度为4
        String checkCode = captcha.getCode();
        //获取经过base64编码处理的图片数据
        String imageData = captcha.getImageBase64();
        //2.生成sessionID转化为String 避免数据精度丢失
        String sessionId = String.valueOf(idWorker.nextId());
        log.info("当前生成的图片验证码:{},会话id:{}",checkCode,sessionId);
        //3.将sessionId作为key 校验码作为value保存在redis中 (使用redis模拟session的行为 还可以设置过期时间)
        redisTemplate.opsForValue().set(StockConstant.CHECK_PREFIX +sessionId,checkCode,5, TimeUnit.MINUTES);
        //4.组装响应数据
        HashMap<String,String> data = new HashMap<>();
        data.put("imageData",imageData);
        data.put("sessionId",sessionId);
        //设置响应数据格式
        return R.ok(data);
    }

    @Override
    public R<PageResult<SysUser>> getAllUserInfo(UserRes userRes) {
        // 设置分页
        PageHelper.startPage(userRes.getPageNum(), userRes.getPageSize());

        // 查询用户（参数可能为空，所以 Mapper 里用动态 SQL）
        List<SysUser> userList = sysUserMapper.getAllUserInfo(
                userRes.getUsername(),
                userRes.getNickName(),
                userRes.getStartTime(),
                userRes.getEndTime()
        );

        // PageInfo 包装分页数据
        PageInfo<SysUser> pageInfo = new PageInfo<>(userList);

        // 封装 PageResult
        PageResult<SysUser> pageResult = new PageResult<>(pageInfo);

        return R.ok(pageResult);
    }

    @Override
    public R addUser(MyUser user) {
        long l = idWorker.nextId();
        user.setId(l);
        int i=  sysUserMapper.addUser(user);
       return i==0?R.error("添加用户失败! 用户已经存在"):R.ok("添加用户成功");
    }

    @Override
    public R<Map<String, Object>> getInfoByUserId(String id) {
        HashMap info=new HashMap();
        List<SysRole>data=sysRoleMapper.getInfoByUserId(id);
        List<SysRole>allData=sysRoleMapper.getAllinfos();
        info.put("ownRoleIds",data);
        info.put("allRole",allData);
        return R.ok(info);
    }

    @Override
    public R upDateroles(UpDateInfos infos) {
        sysUserMapper.DeleteAllroleAndUserInfoByUserId(infos.getUserId());
        int k=sysUserMapper.addDateInfo(infos.getUserId(),infos.getRoleIds());
        return k==0?R.error("更改权限失败!!"):R.ok("更改权限成功!!!");
    }

    @Override
    public R deleteByid(List<Long> ids) {
        int n= sysUserMapper.deleteByIds(ids);
        return n==0?R.error("删除失败"):R.ok("删除成功!");
    }

    @Override
    public R<SysUser> getById(String id) {
        SysUser info=sysUserMapper.GetById(id);
        return info==null?R.error("该用户不存在!"):R.ok(info);

    }

    @Override
    public R updateinfosById(SysUser sysUser) {
       return sysUserMapper.updateById(sysUser)==0?R.error("修改信息失败!"):R.ok("修改信息成功!");

    }

    @Override
    public R<PageResult<SysRole>> getAllinfosByPages(Integer num, Integer size) {

        PageHelper.startPage(num,size);
        List<SysRole> infos=sysRoleMapper.getAllinfosByPages();
        PageInfo<SysRole>pageInfo=new PageInfo<>(infos);//PageHelper 会在 SQL 后面自动加 LIMIT 分页语句： 所以不可以加分号
        PageResult<SysRole>res=new PageResult<>(pageInfo);
        return R.ok(res);
    }

    @Override
    public R deleteByidRoel(String id) {
        return sysUserMapper.deleteByidRoel(id)==0?R.error("删除失败!"):R.ok("删除成功!");
    }

    @Override
    public R<List<SysPermission>> selectAllPermission() {
       List<SysPermission> infp=sysPermissionMapper.findall();
       return R.ok(infp);
    }

    @Override
    public R updateRoleById(HashMap<String, Object> resInfo) {
        String name= (String) resInfo.get("name");
       Long roleId= sysRoleMapper.getIdByName(name);
       Long id= (Long) resInfo.get("id");
       List<Long> permissionIds = (List<Long>) resInfo.get("permissionsIds");
     int n=  sysRolePermissionMapper.add(roleId,id,permissionIds);
return n==0?R.error("修改`信息失败!!"):R.ok("修改信息成功!");
    }

    @Override
    public R deleteRoleById1(Integer id) {
      Integer a= sysRoleMapper.deleteRoleById(id);
      return a==0?R.error("删除失败!!"):R.ok("删除成功@@!");
    }

    @Override
    public R updateStatusById(String id, String status) {
       return sysUserMapper.updateStatusById(id,status)==0?R.error("修改无效!"):R.ok("修改成功!!");
    }

    @Override
    public R addPermission(PermissionVo permissionVo) {
        permissionVo.setId(idWorker.nextId());
     Integer n = sysPermissionMapper.addPermission(permissionVo);
     return n==0?R.error("添加失败 请重新添加!"):R.ok("ok");
    }

    @Override
    public R deleteByidPermiison(String id) {
       return sysPermissionMapper.deleteByidPermiison(id)==0?R.error("delete false"):R.ok("delete suceess!");

    }

    @Override
    public R<List<String>> getAllroles() {
        List<String>infos=sysRoleMapper.getAllroles();
        return R.ok(infos);
    }

    @Override
    public List<RolesDomin> getPermissionTree() {
        List<Map<String, Object>> rows = sysRoleMapper.selectAllPermissions();
        Map<String, RolesDomin> nodeMap = new HashMap<>();
        List<RolesDomin> roots = new ArrayList<>();

        // 构建节点对象
        for (Map<String, Object> row : rows) {
            RolesDomin node = new RolesDomin();
            node.setId(String.valueOf(row.get("id")));
            node.setLabel((String) row.get("name")); // 菜单或权限名称
            node.setPermission((String) row.get("permission"));
            node.setUrl((String) row.get("url"));
            node.setMethod((String) row.get("method"));
            nodeMap.put(node.getId(), node);
        }

        // 构建树结构
        for (Map<String, Object> row : rows) {
            String parentId = String.valueOf(row.get("parent_id"));
            String id = String.valueOf(row.get("id"));
            RolesDomin node = nodeMap.get(id);

            if (parentId == null || parentId.equals("0")) {
                roots.add(node); // 根节点
            } else {
                RolesDomin parent = nodeMap.get(parentId);
                if (parent != null) {
                    parent.getChildren().add(node);
                } else {
                    roots.add(node); // 防止丢失节点
                }
            }
        }

        return roots;
    }

    @Override
    public R addRoles(HashMap<String, Object> infos) {
        String name= (String) infos.get("name");
        String des= (String) infos.get("description");
        List<Long> ids= (List<Long>) infos.get("permissionsIds");
        long roleid = idWorker.nextId();
        sysRoleMapper.addUser(roleid,name,des);
        sysRolePermissionMapper.addinfo(roleid,ids);
return R.ok("add true!");
    }


}

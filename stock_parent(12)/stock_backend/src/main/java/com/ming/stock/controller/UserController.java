package com.ming.stock.controller;

import cn.hutool.captcha.LineCaptcha;
import com.ming.stock.pojo.domain.PermisssionDomin;
import com.ming.stock.pojo.domain.RolesDomin;
import com.ming.stock.pojo.entity.SysUser;
import com.ming.stock.service.UserService;
import com.ming.stock.vo.req.LoginReqVo;
import com.ming.stock.vo.resp.LoginRespVo;
import com.ming.stock.vo.resp.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Ming
 * @Description TODO
 */
@RestController
@RequestMapping("/api")
@Tag(name = "用户相关接口处理器")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 根据用户查询用户信息
     * @param userName
     * @return
     */
    @Operation(summary = "根据用户名查询用户信息",responses = {
            @ApiResponse(responseCode = "200",
                    description = "成功获取用户信息",
                    content = @Content(schema = @Schema(implementation = SysUser.class)))
    })
    @GetMapping("/user/{userName}")
    public SysUser getUserByUserName(@PathVariable("userName") String userName){
        return userService.getUserByUserName(userName);
    }

    /**
     * 用户登录功能
     * @param vo
     * @return
     */
    @Operation(summary = "用户登录功能",responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功登录",
                    content = @Content(schema = @Schema(implementation = LoginRespVo.class)))
    })
    @PostMapping("/login")
    public R<LoginRespVo> login(@RequestBody LoginReqVo vo){//传给前端的需要进行序列化的转化
        return userService.login(vo);
    }
    @Operation(summary = "验证码生成",responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功获取验证码",
                    content = @Content(schema = @Schema(implementation = Map.class))
            )
    })
    @GetMapping("/captcha")
    public R<Map> getCaptCode(){
        return userService.getCaptCode();
    }
@GetMapping("/permissions/tree/all")
public R<List<RolesDomin>> getPermissionTree() {
    List<RolesDomin> tree = userService.getPermissionTree();
    return R.ok(tree);
}
@PostMapping("/role")
    public R addRoles(@RequestBody HashMap<String,Object>infos){
return userService.addRoles(infos);
}

}

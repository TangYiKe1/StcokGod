package com.ming.stock.controller;

import com.ming.stock.pojo.entity.SysUser;
import com.ming.stock.pojo.my.MyUser;
import com.ming.stock.service.UserService;

import com.ming.stock.vo.req.my.UserRes;
import com.ming.stock.vo.resp.PageResult;
import com.ming.stock.vo.resp.R;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MyUserCon {
    @Autowired
    private UserService userService;
    @PostMapping("/users")
    public R<PageResult<SysUser>> getAllUserInfo(@RequestBody UserRes userRes) {
        return userService.getAllUserInfo(userRes);
    }
    @PostMapping("user")
    public R addUser(@RequestBody MyUser user){
return userService.addUser(user);
    }
}

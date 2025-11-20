package com.ming.stock.controller;

import com.ming.stock.pojo.entity.SysPermission;
import com.ming.stock.pojo.entity.SysRole;
import com.ming.stock.pojo.entity.SysUser;
import com.ming.stock.service.StockService;
import com.ming.stock.service.UserService;
import com.ming.stock.pojo.my.PermissionVo;
import com.ming.stock.vo.req.my.UpDateInfos;
import com.ming.stock.vo.resp.PageResult;
import com.ming.stock.vo.resp.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class MyUserC1 {
    @Autowired
    private UserService userService;
    @Autowired
    private StockService stockService;
    @GetMapping("/api/user/roles/{userId}")
    public R<Map<String,Object>> getInfoByUserId(@PathVariable(name = "userId") String id){
return userService.getInfoByUserId(id);
    }

    /**
     * 1.4 更新用户角色信息接口说明
     * @param infos
     * @return
     */
    @PutMapping("/api/user/roles")
    public R upDate(@RequestBody UpDateInfos infos){
      return   userService.upDateroles(infos);
    }
//@DeleteMapping("/user")
//    public R deleteById(@RequestBody List<Long> ids){
//        return userService.deleteByid(ids);
//}
@DeleteMapping("/user")
public R deleteById(@RequestBody List<String> ids) {
    // 将字符串列表转成 Long 列表
    List<Long> longIds = ids.stream()
            .map(Long::valueOf)
            .collect(Collectors.toList());

    return userService.deleteByid(longIds);
}

@GetMapping("/api/user/info/{userId}")
    public R<SysUser> getinfoById1_6(@PathVariable(name = "userId") String id){
     return userService.getById(id);
}

    /**
     * 1.7 更新用户信息
     * 根据id进行修改
     * @param sysUser
     * @return
     */
    @PutMapping("/api/user")
    public R upDateInfos(@RequestBody SysUser sysUser){
      return   userService.updateinfosById(sysUser);
}
@PostMapping("/api/roles")
    public R<PageResult<SysRole>> getAllinfosByPages(
            @RequestParam(defaultValue = "1",name = "pageNum")Integer num,
            @RequestParam(defaultValue = "10",name = "pageSize")Integer size
){
return userService.getAllinfosByPages(num,size);
}
    /**
     * 2.5 根据角色id删除角色信息
     */
@DeleteMapping("/api/role/{roleId}")
    public R deleteUserById(@PathVariable(name = "roleId") String id){
        return userService.deleteByidRoel(id);
}
@GetMapping("/api/permissions")
    public R<List<SysPermission>> selectAllPermission(){
     return userService.selectAllPermission();
}
/**
 * 2.4 更新角色信息，包含角色关联的权限信息
 */
@PutMapping("/api/role")
    public R updateRoleById(@RequestBody HashMap<String,Object>resInfo){
      return   userService.updateRoleById(resInfo);
}

//@DeleteMapping("/api/role/{roleId}")
//    public R deleteRoleById1(@PathVariable(name = "roleId") Integer id){
//    return userService.deleteRoleById1(id);
//}
    /**
     * 更新角色的状态信息
     */
@PostMapping("/api/role/{roleId}/{status}")
    public R updateStatusById(@PathVariable(name = "roleId") String id,@PathVariable(name = "status")String status){
    return userService.updateStatusById(id,status);
}
/**
 * 3.4 权限添加按钮
 *
 */
@PostMapping("/api/permission")
    public R addPermission(@RequestBody PermissionVo permissionVo){
    return userService.addPermission(permissionVo);
}
@GetMapping("/quot/stock/search")
    public R<List<Map>> selectNotClaer(@RequestParam(name = "searchStr" ) String resInfo){
   return stockService.selectNotClaer(resInfo);
}
@PutMapping("/api/permission")
    public R addPermission1(@RequestBody PermissionVo vo){
    return userService.addPermission(vo);
}
@DeleteMapping("/api/permission/{permissionId}")
    public R deleteById(@PathVariable(name = "permissionId") String id){
    return userService.deleteByidPermiison(id);
}

}

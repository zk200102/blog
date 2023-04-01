package com.zk.blogapi.controller;


import com.zk.blogapi.service.SysUserService;
import com.zk.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zk
 * @since 2023-03-26
 */
@RestController
@RequestMapping("/users")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;


    @ApiOperation("根据token查询用户信息")
    @GetMapping ("currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token){
        return sysUserService.getUserByToken(token);
    }
}


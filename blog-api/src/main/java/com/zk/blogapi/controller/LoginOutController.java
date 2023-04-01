package com.zk.blogapi.controller;

import com.zk.blogapi.service.LoginService;
import com.zk.blogapi.service.SysUserService;
import com.zk.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zk
 * @date 2023/3/29 12:26
 * @desciption: 退出登录
 */
@RestController
@RequestMapping("logout")
public class LoginOutController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private LoginService loginService;

    @ApiOperation("退出登录")
    @GetMapping
    public Result logout(@RequestHeader("Authorization") String token){
        return loginService.logout(token);
    }

    @ApiOperation("测试枚举类型数据是否正常展示")
    @GetMapping("getUserInfo/{account}")
    public Result getUserInfo(@PathVariable String account){
        return sysUserService.getUserInfo(account);
    }
}

package com.zk.blogapi.controller;

import com.zk.blogapi.service.LoginService;
import com.zk.blogapi.vo.param.LoginParam;
import com.zk.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * author zk
 * date 2023/3/28 16:11
 * description: 登陆控制器
 */
@RestController
@RequestMapping("login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping
    @ApiOperation("登陆")
    public Result login(@RequestBody LoginParam loginParam){
        return loginService.login(loginParam.getAccount(),loginParam.getPassword());
    }
}

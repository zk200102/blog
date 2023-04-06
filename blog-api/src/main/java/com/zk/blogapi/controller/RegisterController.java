package com.zk.blogapi.controller;

import com.zk.blogapi.annotation.LogAnnotation;
import com.zk.blogapi.service.LoginService;
import com.zk.blogapi.utils.enums.LogTypeEnum;
import com.zk.blogapi.vo.RegisterVo;
import com.zk.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author zk
 * date 2023/3/30 10:21
 * description: 注册控制器
 */
@RestController
@RequestMapping("register")
public class RegisterController {
    @Autowired
    private LoginService loginService;

    @PostMapping
    @ApiOperation("注册")
    @LogAnnotation(model = "注册",logType = LogTypeEnum.ADD,desc = "注册")
    public Result register(@RequestBody RegisterVo registerVo){
        return loginService.register(registerVo);
    }
}

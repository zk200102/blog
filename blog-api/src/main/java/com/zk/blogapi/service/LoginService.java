package com.zk.blogapi.service;

import com.zk.blogapi.vo.RegisterVo;
import com.zk.common.Result;

/**
 * author zk
 * date 2023/3/28 19:30
 * description:
 */

public interface LoginService {
    /**
     * author: zk
     * date: 2023/3/30
     * description: 登陆实现
     * param username: 用户名
     * param password: 密码
     * return: com.zk.common.Result
     */
    Result login(String username, String password);
    /**
     * author: zk
     * date: 2023/3/30
     * description: 退出登陆
     * param token: 用户token
     * return: com.zk.common.Result
     */
    Result logout(String token);
    /**
     * author: zk
     * date: 2023/3/30
     * description: 注册登陆
     * param registerVo: 注册参数实体类
     * return: com.zk.common.Result
     */
    Result register(RegisterVo registerVo);
}

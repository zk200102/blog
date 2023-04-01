package com.zk.blogapi.service;

import com.zk.blogapi.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zk.common.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * author zk
 * @since 2023-03-26
 */
public interface SysUserService extends IService<SysUser> {
    /**
     * author: zk
     * date: 2023/3/30
     * description: 根据用户名密码查询用户
     * param username: 用户名
     * param password: 密码
     * return: com.zk.blogapi.entity.SysUser
     */
    SysUser findByUsernameAndPassword(String username, String password);
    /**
     * author: zk
     * date: 2023/3/30
     * description: 获取token里的用户信息
     * param token: 用户token
     * return: com.zk.common.Result
     */
    Result getUserByToken(String token);
    /**
     * author: zk
     * date: 2023/3/30
     * description: 获取用户信息，测试枚举类型数据是否正常展示
     * return: com.zk.common.Result
     */
    Result getUserInfo(String account);
    /**
     * author: zk
     * date: 2023/4/1
     * description: 根据用户名查询用户
     * param account: 用户名
     * return: com.zk.blogapi.entity.SysUser
     */
    SysUser findUserByUsername(String account);
}

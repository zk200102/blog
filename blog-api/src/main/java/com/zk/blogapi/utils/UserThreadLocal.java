package com.zk.blogapi.utils;

import com.zk.blogapi.entity.SysUser;

/**
 * author zk
 * date 2023/3/30 14:12
 * description:把用户信息存储到ThreadLocal，并提供相关方法操作
 */
public class UserThreadLocal {
//    定义私有化构造，禁止该类实例化
    private UserThreadLocal(){}
    private final static ThreadLocal<SysUser> USER = new ThreadLocal<>();
    public static void put(SysUser user){
        USER.set(user);
    }
    public static SysUser getAccount() {
        return USER.get();
    }
    public static void remove(){
        USER.remove();
    }
}

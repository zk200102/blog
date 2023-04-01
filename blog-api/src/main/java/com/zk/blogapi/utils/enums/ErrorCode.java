package com.zk.blogapi.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zk
 * @date 2023/3/28 19:36
 * @desciption: 错误码枚举
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {
    PARAM_ERROR(10001,"参数错误！"),
    ACCOUNT_PWD_NOT_EXIST(10002,"用户名或密码错误！"),
    TOKEN_ERROR(10003,"token不合法！"),
    ACCOUNT_EXIST(10004,"用户已存在!"),
    NO_PERMISSION(70001,"暂无访问权限！"),
    SESSION_TIME_OUT(90001,"会话超时！"),
    NO_LOGIN(90002,"请登录！");
    private final int code;
    private final String msg;
}

package com.zk.blogapi.vo.param;

import lombok.Data;

/**
 * author zk
 * date 2023/3/29 12:10
 * description: 接收登陆参数
 */
@Data
public class LoginParam {
    private String account;
    private String password;
}

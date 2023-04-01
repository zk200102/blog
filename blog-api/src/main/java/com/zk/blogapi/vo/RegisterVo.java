package com.zk.blogapi.vo;

import lombok.Data;

/**
 * @author zk
 * @date 2023/3/30 9:58
 * @desciption: 注册实体类
 */
@Data
public class RegisterVo {
    private String account;
    private String password;
    private String nickname;
}

package com.zk.blogapi.vo;

import lombok.Data;

/**
 * author zk
 * date 2023/3/29 11:35
 * description: 登陆用户信息实体类
 */
@Data
public class LoginUserVo {
    private Long id;
    private String account;
    private String nickname;
    private String avatar;
}

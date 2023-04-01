package com.zk.blogapi.utils.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * author zk
 * date 2023/3/30 12:40
 * description: 是否是管理员枚举
 */

public enum AdminEnum {
    ADMIN_TRUE(true,1),
    ADMIN_FALSE(false,0);
    @EnumValue
    private final boolean admin;
    @JsonValue
    private final int isAdmin;

    AdminEnum(boolean admin, int isAdmin) {
        this.admin = admin;
        this.isAdmin = isAdmin;
    }
}

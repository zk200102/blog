package com.zk.blogapi.utils.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author zk
 * @date 2023/3/30 11:50
 * @desciption: 删除枚举
 */
public enum DeleteEnum {

    DELETE_TRUE(true,1),
    DELETE_FALSE(false,0);
    //@EnumValue标记数据库使用的字段，@JsonValue标记要展示的字段
    @EnumValue
    private final boolean deleted;
    @JsonValue
    private final int isDeleted;
    DeleteEnum(boolean deleted, int isDeleted) {
        this.deleted = deleted;
        this.isDeleted = isDeleted;
    }
}

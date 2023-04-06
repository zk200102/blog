package com.zk.blogapi.utils.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * author: zk
 * date: 2023/4/6
 * description: 日志操作枚举
 */

@Getter
@AllArgsConstructor
public enum LogTypeEnum {
    /**
     * 0=其它,1=新增,2=修改,3=删除,4=授权,5=导出,6=导入,7=强退,8=登录,9=清空数据,10查询
     * */
    OTHER(0,"其它"),
    ADD(1,"新增"),
    UPDATE(2,"修改"),
    DEL(3,"删除"),
    AUTH(4,"授权"),
    EXPORT(5,"导出"),
    IMPORT(6,"导入"),
    QUIT(7,"强退"),
    GENERATE_CODE(8,"登录"),
    CLEAR(9,"清空"),
    QUERY(10,"查询");

    private int value;
    @EnumValue
    @JsonValue
    private String desc;
}


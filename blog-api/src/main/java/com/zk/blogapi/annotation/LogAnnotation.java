package com.zk.blogapi.annotation;

import com.zk.blogapi.utils.enums.LogTypeEnum;

import java.lang.annotation.*;

/**
 * author: zk
 * date: 2023/4/6
 * description: 日志注解
 */
@Target(ElementType.METHOD)//注解放置的目标位置即方法级别
@Retention(RetentionPolicy.RUNTIME)//注解在哪个阶段执行
@Documented
public @interface LogAnnotation {
    /**
     * 模块
     * */
    String model() default "";
    /**
     * 操作
     * */
    LogTypeEnum logType() default LogTypeEnum.OTHER;

    String desc() default "";  // 操作说明

    boolean save() default true; //是否将当前日志记录到数据库中
}

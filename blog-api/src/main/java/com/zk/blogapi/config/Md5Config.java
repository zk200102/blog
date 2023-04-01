package com.zk.blogapi.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author zk
 * @date 2023/3/31
 * @description: md5配置类
 */
@Configuration
public class Md5Config implements InitializingBean {
    @Value("${md5.slot}")
    private String slot;
    public static String MD5_SLOT;

    @Override
    public void afterPropertiesSet() {
        MD5_SLOT = slot;
    }
}

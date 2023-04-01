package com.zk.blogapi.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zk
 * @date 2023/3/31
 * @description: jwt配置文件
 */
@Data
@Configuration
@ConfigurationProperties("jwt")
public class JwtConfig implements InitializingBean {

    private String secret;
    private Long expirationTime;
    public static String JWT_SECRET;
    public static Long JWT_EXPIRATION;

    @Override
    public void afterPropertiesSet() {
        JWT_SECRET = secret;
        JWT_EXPIRATION = expirationTime;
    }
}

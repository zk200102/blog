package com.zk.common;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author zk
 * @date 2023/3/26 14:59
 * @desciption: 配置swagger
 */
@EnableSwagger2
@EnableKnife4j
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket webApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2).groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
//                .paths(Predicates.not(PathSelectors.regex("/admin/.*")))
//                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .paths(PathSelectors.any())
                .build();
//                处理security授权访问的问题
//                .securityContexts(securityContexts())
//                .securitySchemes(securitySchemes());
    }

    private ApiInfo webApiInfo() {
        return new ApiInfoBuilder()
                .title("网站-blogAPI文档")
                .description("本文档描述了blog接口定义")
                .version("1.0")
                .contact(new Contact("zk", "locahost:80", "2762560464@qq.com"))
                .build();
    }

}

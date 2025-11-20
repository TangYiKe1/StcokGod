package com.ming.stock.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Ming
 * @Description: Springdoc OpenAPI 配置类
 **/
@Configuration
@EnableKnife4j
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        Contact contact = new Contact()
                .name("Ming")
                .url("https://www.Ming.com/")
                .email("1151939691@163.com");

        return new OpenAPI()
                .info(new Info()
                        .title("股票指数-在线接口API文档")
                        .description("这是一个方便前后端开发人员快速了解开发接口需求的在线接口API文档")
                        .contact(contact)
                        .version("1.0.0")
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .packagesToScan("com.ming.stock.controller")
                .pathsToMatch("/**")
                .build();
    }
}
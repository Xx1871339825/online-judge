package com.crowoj.api.core.config;

import com.crowoj.api.core.enums.ResultEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author crow
 * @create 2021/11/2 16:40
 * @description
 */
@Configuration
public class Swagger3config {
    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.OAS_30)
                .globalResponses(HttpMethod.GET,getResponseList())
                .globalResponses(HttpMethod.POST,getResponseList())
                .globalResponses(HttpMethod.PUT,getResponseList())
                .globalResponses(HttpMethod.DELETE,getResponseList())
                .securitySchemes(Collections.singletonList(HttpAuthenticationScheme.JWT_BEARER_BUILDER
//                        显示用
                        .name("Authorization")
                        .build()))
                .securityContexts(Collections.singletonList(SecurityContext.builder()
                        .securityReferences(Collections.singletonList(SecurityReference.builder()
                                .scopes(new AuthorizationScope[0])
                                .reference("Authorization")
                                .build()))
                        // 声明作用域
                        .operationSelector(o -> o.requestMappingPattern().matches("/.*"))
                        .build()))
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.crowoj.api.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("CrowOj接口文档")
                .description("更多请咨询服务开发者Crow。")
                .contact(new Contact("Crow", null, "1871339825@qq.com"))
                .version("1.0")
                .build();
    }

    private List<Response> getResponseList(){
        var list = new ArrayList<Response>();
        Arrays.stream(ResultEnum.values()).forEach(err ->{
            list.add(new ResponseBuilder()
                    .code(err.getStatus()+"")
                    .description(err.getMessage())
                    .build()
            );
        });
        return list;
    }


}

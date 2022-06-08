package com.crowoj.judgeserver.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author crow
 * @create 2022/2/18 22:36
 * @description
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(getJwtInterceptor())
//                .addPathPatterns("/**");
//    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .maxAge(3600)
                .allowCredentials(true);
    }
}

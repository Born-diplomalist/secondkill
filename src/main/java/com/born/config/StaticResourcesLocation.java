package com.born.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description: 配置静态资源映射
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-10-21 10:23:25
 */
@Configuration
public class StaticResourcesLocation implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/**").addResourceLocations("classpath:/templates/");
   }
}
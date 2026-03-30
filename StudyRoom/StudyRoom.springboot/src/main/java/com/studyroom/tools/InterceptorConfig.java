package com.studyroom.tools;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc的配置
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 配置拦截器（/Captcha/** 无需JWT，加入白名单）
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CurrentUserInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/Captcha/**");
    }

    /**
     * 配置跨域（allowCredentials 支持 Session cookie 跨域传递）
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    /**
     * 资源的配置处理
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String filePath = System.getProperty("user.dir");
        String location = "file:" + filePath + "\\src\\main\\resources\\static\\";
        registry.addResourceHandler("/**").addResourceLocations(location);
    }
}

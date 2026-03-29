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
     * 配置切面
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //获取当前上下文用户信息拦截器
        registry.addInterceptor(new CurrentUserInterceptor())
                .addPathPatterns("/**");
    }
    /**
     * 配置前后端跨域报错的问题
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
    /**
     * 资源的配置处理
     */
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //获取项目运行的绝对路径
        String filePath = System.getProperty("user.dir");
        //定义一个存放图片的文件目录
        String localtion="file:"+filePath+"\\src\\main\\resources\\static\\";
        // 只要是/images/** 开头的请求网址  都会去上面的localtion中寻找资讯
        registry.addResourceHandler("/**").
                addResourceLocations(localtion);
    }
}

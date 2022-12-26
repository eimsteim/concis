package com.joycoho.concis.sys.authc.config;

import cn.hutool.core.collection.CollectionUtil;
import com.joycoho.concis.sys.authc.interceptor.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Classname WebConfig
 * @Description 全局拦截路由拦截配置
 * @Version 1.0.0
 * @Date 2022/10/5 23:44
 * @Created by Leo
 */
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private WebConfigProperties configProperties;

    @Bean
    public static PropertySourcesPlaceholderConfigurer getPropertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册LoginInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new LoginInterceptor());
        //添加被拦截路径
        List<String> addPathPatterns = configProperties.getAddPathPatterns();
        if (CollectionUtil.isNotEmpty(addPathPatterns)) {
            registration.addPathPatterns(addPathPatterns);
        } else {
            //缺省拦截所有
            registration.addPathPatterns(new String[]{"/**"});
        }
        //添加不拦截路径
        List<String> excludePathPatterns = configProperties.getExcludePathPatterns();
        if (CollectionUtil.isNotEmpty(excludePathPatterns)) {
            registration.excludePathPatterns(excludePathPatterns);
        } else {
            registration.excludePathPatterns(    //添加不拦截路径
                    "/user/login",               //登录页面
                    "/user/login/**",   //登录请求路径
                    "/error",           //错误
                    "/**/*.html",                //html静态资源
                    "/**/*.js",                  //js静态资源
                    "/**/*.css",                 //css静态资源
                    "/**/*.jpg",                 //css静态资源
                    "/**/*.jpeg",                 //css静态资源
                    "/**/*.png",                 //css静态资源
                    "/**/*.bmp"                  //css静态资源
            );
        }
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .allowedMethods("*")
                .maxAge(3600);
    }
}

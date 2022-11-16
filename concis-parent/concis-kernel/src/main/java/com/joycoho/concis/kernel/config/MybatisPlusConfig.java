package com.joycoho.concis.kernel.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname MybatisPlusConfig
 * @Description MybtisPlus拦截器配置
 * @Version 1.0.0
 * @Date 2022/10/30 14:30
 * @Created by Leo
 */
@Configuration
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(DbType.MYSQL);//设置MySQL方言
        paginationInnerInterceptor.setOverflow(true);//设置请求的页数超过最大页数时的动作，true:回到第一页，false:继续请求，默认为false
        paginationInnerInterceptor.setMaxLimit(500L);//设置最大单页限制数量，默认500条，-1不受限制
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }
}

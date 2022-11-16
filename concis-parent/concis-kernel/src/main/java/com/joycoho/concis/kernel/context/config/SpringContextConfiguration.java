package com.joycoho.concis.kernel.context.config;

import com.joycoho.concis.kernel.context.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

/**
 * @Classname SpringContextConfiguration
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/6/22 17:20
 * @Created by Leo
 */
@Slf4j
//@AutoConfigureOrder(1)
public class SpringContextConfiguration {

    @Bean
    public SpringContextHolder springContextHolder() {
        log.info("开始初始化 SpringContextHolder");
        return new SpringContextHolder();
    }
}

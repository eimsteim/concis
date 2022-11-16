package com.joycoho.concis.sys.authc.config;

import cn.hutool.core.util.StrUtil;
import com.joycoho.concis.kernel.authc.api.IAuthcValidator;
import com.joycoho.concis.kernel.authc.config.AuthcConfiguration;
import com.joycoho.concis.sys.authc.api.DefaultAuthcValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Slf4j
@Configuration
public class DefaultAuthcConfiguration extends AuthcConfiguration {

    @Value("${concis.authc.validator}")
    private String authcValidatorClassName;
    /**
     * 定义的用于返回鉴权类型的钩子方法
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(IAuthcValidator.class)
    @DependsOn("springContextHolder")
    public IAuthcValidator authcValidator() {
        log.info("开始初始化 DefaultAuthcValidator");
        if (StrUtil.isNotBlank(authcValidatorClassName)) {
            try {
                Class _class = Class.forName(authcValidatorClassName);
                return (IAuthcValidator) _class.newInstance();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("初始化默认authcValidator结束");
        return new DefaultAuthcValidator();
    }
}

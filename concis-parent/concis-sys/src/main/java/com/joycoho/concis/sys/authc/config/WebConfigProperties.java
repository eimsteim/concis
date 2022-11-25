package com.joycoho.concis.sys.authc.config;

import lombok.Data;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Classname WebConfigProperties
 * @Description 从配置文件中读取MVC相关配置
 * @Version 1.0.0
 * @Date 2022/11/26 00:47
 * @Created by Leo
 */
@Data
@Component
@ConfigurationProperties(prefix = "concis.mvc")
public class WebConfigProperties {
    @Setter
    private List<String> addPathPatterns;

    @Setter
    private List<String> excludePathPatterns;
}

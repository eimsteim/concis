//package com.joycoho.concis.kernel.db.druid.config;
//
//
//import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
//import com.joycoho.concis.kernel.db.druid.config.properties.DruidProperties;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//
//import javax.sql.DataSource;
//
//@Slf4j
//public class SingleDataSourceConfig {
//    /**
//     * druid配置
//     */
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DruidProperties druidProperties() {
//        return new DruidProperties();
//    }
//
////    /**
////     * 单数据源连接池配置
////     */
////    @Primary
////    @Bean("ds0")
////    public DruidDataSource ds0(DruidProperties druidProperties) {
////        log.info("开始初始化 SingleDataSourceConfig");
////        DruidDataSource dataSource = new DruidDataSource();
////        druidProperties.config(dataSource);
////        return dataSource;
////    }
//
////    @Bean("sqlManager0")
////    public SQLManager sqlManager0(DruidDataSource ds0) {
////        ConnectionSource source = ConnectionSourceHelper.getSingle(ds0);
////        return SQLManager.newBuilder(source).build();
////    }
//}

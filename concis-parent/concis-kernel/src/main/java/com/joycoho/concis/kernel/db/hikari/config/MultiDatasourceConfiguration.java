package com.joycoho.concis.kernel.db.hikari.config;

import com.joycoho.concis.kernel.db.hikari.config.properties.HikariDatasourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

//@Configuration
public class MultiDatasourceConfiguration {

//    /**
//     * 第一个数据源的配置
//     * @return
//     */
//    @Bean
//    @ConfigurationProperties(prefix = "spring.ds0")
//    public HikariDatasourceProperties ds0Properties() {
//        return new HikariDatasourceProperties();
//    }
//
//    @Primary
//    @Bean(name = "ds0")
//    public DataSource ds0(HikariDatasourceProperties ds0Properties) {
//        HikariDataSource dataSource = new HikariDataSource(ds0Properties.getHikari());
//        ds0Properties.config(dataSource);
//        return dataSource;
//    }
}

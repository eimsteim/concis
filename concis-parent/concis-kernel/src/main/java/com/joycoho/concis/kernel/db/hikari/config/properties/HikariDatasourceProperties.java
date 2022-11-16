package com.joycoho.concis.kernel.db.hikari.config.properties;

import lombok.Data;

@Data
public class HikariDatasourceProperties {

    private String url;

    private String username;

    private String password;

    private String driverClassName;

    private String filters;

    private String type;

//    private HikariConfig hikari;
//
//    public void config(HikariDataSource dataSource) {
//        dataSource.setJdbcUrl(this.url);
//        dataSource.setUsername(this.username);
//        dataSource.setPassword(this.password);
//        dataSource.setDriverClassName(this.driverClassName);
//
//    }
}

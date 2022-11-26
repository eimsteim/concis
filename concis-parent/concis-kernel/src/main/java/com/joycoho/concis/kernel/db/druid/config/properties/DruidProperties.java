package com.joycoho.concis.kernel.db.druid.config.properties;


import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;

import java.sql.SQLException;

/**
 * <p>数据库数据源配置</p>
 * <p>说明:类中属性包含默认值的不要在这里修改,应该在"application.yml"中配置</p>
 */
@Data
public class DruidProperties {

    private String url;

    private String username;

    private String password;

    private String driverClassName;
    /**
     * 初始化时建立的物理连接个数
     */
    private Integer initialSize = 2;
    /**
     * 连接池最小连接数
     */
    private Integer minIdle = 1;
    /**
     * 连接池最大连接数
     */
    private Integer maxActive = 20;
    /**
     * 获取连接时最大等待时间，单位毫秒
     */
    private Integer maxWait = 60000;

    private Integer timeBetweenEvictionRunsMillis = 60000;
    /**
     * 销毁线程时检测当前连接的最后活动时间和当前时间的差，大于该值时，关闭当前连接
     */
    private Integer minEvictableIdleTimeMillis = 300000;
    /**
     * 用来检测连接是否有效的SQL，必须是一个查询语句
     * MySQL中的为 select 'x'
     * Oracle中的为 select 1 from dual
     */
    private String validationQuery = "SELECT 'x'";
    /**
     * 申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效
     */
    private Boolean testWhileIdle = true;
    /**
     * 申请连接时会执行validationQuery检测连接是否有效，开启会降低性能，默认为true
     */
    private Boolean testOnBorrow = false;
    /**
     * 归还连接时会执行validationQuery检测连接是否有效，开启会降低性能，默认为true
     */
    private Boolean testOnReturn = false;
    /**
     * 是否缓存preparedStatement，MySQL5.5+建议开启
     */
    private Boolean poolPreparedStatements = true;
    /**
     * 当值大于0时poolPreparedStatements会自动修改为true
     */
    private Integer maxPoolPreparedStatementPerConnectionSize = 20;

    private String filters = "stat";

    public void config(DruidDataSource dataSource) {

        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        dataSource.setDriverClassName(driverClassName);
        dataSource.setInitialSize(initialSize);     //定义初始连接数
        dataSource.setMinIdle(minIdle);             //最小空闲
        dataSource.setMaxActive(maxActive);         //定义最大连接数
        dataSource.setMaxWait(maxWait);             //最长等待时间

        // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);

        // 配置一个连接在池中最小生存的时间，单位是毫秒
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(testOnReturn);

        // 打开PSCache，并且指定每个连接上PSCache的大小
        dataSource.setPoolPreparedStatements(poolPreparedStatements);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);

        try {
            dataSource.setFilters(filters);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

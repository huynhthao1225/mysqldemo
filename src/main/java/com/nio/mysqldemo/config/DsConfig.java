package com.nio.mysqldemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DsConfig {

    @Value("${ds.driver}")
    private String driverName;
    @Value("${ds.url}")
    private String url;
    @Value("${ds.user}")
    private String user;
    @Value("${ds.password}")
    private String password;
    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(driverName); //"com.mysql.cj.jdbc.Driver");
        dataSourceBuilder.url(url); //"jdbc:mysql://localhost:3306/sakila");
        dataSourceBuilder.username(user); //"user1");
        dataSourceBuilder.password(password); //"Mtvh11MySql");
        return dataSourceBuilder.build();
    }
}

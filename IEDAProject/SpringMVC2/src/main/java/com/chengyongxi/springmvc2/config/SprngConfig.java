package com.chengyongxi.springmvc2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DelegatingDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.chengyongxi.Service")
public class SprngConfig {
    @Bean
    public DataSource dataSource(){
        DataSource dataSource = new DelegatingDataSource();
//        dataSource.setDrivename;
        return dataSource;
    }
}

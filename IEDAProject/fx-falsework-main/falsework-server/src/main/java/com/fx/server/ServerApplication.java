package com.fx.server;

import com.fx.server.util.DBLog;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan("com.fx.server.mapper")
@SpringBootApplication(exclude=SecurityAutoConfiguration.class)
public class ServerApplication {

    public static void main(String[] args) {
        DBLog.getInstance().start();
        SpringApplication.run(ServerApplication.class, args);
    }

}

package com.exampletest.testtt.Configurations;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;


import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;

import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@Data
public class DataBaseConfigure {

    @Bean(name = "threadPoolTaskFreeOrders")
    public Executor asyncExecutor() {
        return Executors.newFixedThreadPool(6);
    }

//    @Bean(initMethod = "start", destroyMethod = "stop")
//    public Server fileyH2DatabaseaServer() throws SQLException {
//        return Server.createTcpServer(
//                "-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
//    }

//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.h2.Driver");
//        dataSource.setUrl("jdbc:h2:file:./db/orders.db");
//        dataSource.setUsername("sa");
//        dataSource.setPassword("");
//        return dataSource;
//    }



}

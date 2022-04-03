package com.dhivakar.filetransferhelper.database;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {


    @Bean
    @Profile("dev")
    public DataSource devDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/ImportDBDev");
        dataSource.setUsername("dhiva_dev");
        dataSource.setPassword("devmysql");

        return dataSource;
    }

    @Bean
    @Profile("prod")
    public DataSource prodDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/ImportDB");
        dataSource.setUsername("dhiva_dev");
        dataSource.setPassword("devmysql");

        return dataSource;
    }


}

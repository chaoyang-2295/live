package com.huashengke.com.live.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;


@Configuration
@PropertySource("classpath:db.properties")
public class DatasourceConfig {

    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "db.live")
    public DataSource secondDataSource() {
        return DataSourceBuilder.create().build();
    }
    
}

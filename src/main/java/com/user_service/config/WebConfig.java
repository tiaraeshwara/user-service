package com.user_service.config;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean(name = "userDatabase")
    @ConfigurationProperties(prefix = "spring.datasource-user")
    public DataSource userDatabaseDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "userJdbcTemplate")
    public JdbcTemplate userJdbcTemplate(@Qualifier("userDatabase") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}

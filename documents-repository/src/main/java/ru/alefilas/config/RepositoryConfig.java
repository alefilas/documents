package ru.alefilas.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("ru.alefilas")
public class RepositoryConfig {

    @Bean
    public BasicDataSource dataSource() {
        return new BasicDataSource();
    }

}

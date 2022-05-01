package com.eggseller.test.configure;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {
	@Primary
	@Bean(name = "testDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.test-db")
	public DataSource testDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}
	
	@Bean(name = "eggsellerDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.eggseller-db")
	public DataSource eggsellerDataSource() {
		return new HikariDataSource();
	}
}

package com.eggseller.test.configure;

import java.util.HashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JpaConfig {
	private final DatabaseConfig dataSourceConfig;
	
	@PropertySource({ "classpath:application.yml" })
	@EnableJpaRepositories(
	    basePackages = "com.jpa.master.repository", // Master Repository 경로
	    entityManagerFactoryRef = "masterEntityManager", 
	    transactionManagerRef = "masterTransactionManager"
	)
	@RequiredArgsConstructor
	public class MainDatabaseConfig {
		private final Environment env;

		@Bean
		@Primary
		public LocalContainerEntityManagerFactoryBean masterEntityManager() {
			LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//			em.setDataSource(masterDataSource());
			em.setDataSource(dataSourceConfig.testDataSource());
	        
			//Entity 패키지 경로
	        em.setPackagesToScan(new String[] { "com.jpa.master.entity" });
		
			HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
			em.setJpaVendorAdapter(vendorAdapter);
	        
	        //Hibernate 설정
			HashMap<String, Object> properties = new HashMap<>();
			properties.put("hibernate.hbm2ddl.auto",env.getProperty("hibernate.hbm2ddl.auto"));
			properties.put("hibernate.dialect",env.getProperty("hibernate.dialect"));
			em.setJpaPropertyMap(properties);
			return em;
		}
		
//		@Primary
//		@Bean
//		@ConfigurationProperties(prefix="spring.datasource.test-db")
//		public DataSource masterDataSource() {
//			return DataSourceBuilder.create().build();
//		}
		
		@Primary
		@Bean
		public PlatformTransactionManager masterTransactionManager() {	
			JpaTransactionManager transactionManager = new JpaTransactionManager();
			transactionManager.setEntityManagerFactory(masterEntityManager().getObject());
			return transactionManager;
		}
	}
	
	@PropertySource({ "classpath:application.yml" })
	@EnableJpaRepositories(
	    basePackages = "com.jpa.second.repository", // Second Repository 경로
	    entityManagerFactoryRef = "secondEntityManager", 
	    transactionManagerRef = "secondTransactionManager"
	)
	@RequiredArgsConstructor
	public class SecondConfig {
		private final Environment env;

		@Bean
		public LocalContainerEntityManagerFactoryBean secondEntityManager() {
			LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
			em.setDataSource(dataSourceConfig.eggsellerDataSource());
			em.setPackagesToScan(new String[] { "com.jpa.master.entity" });

			HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
			em.setJpaVendorAdapter(vendorAdapter);
			HashMap<String, Object> properties = new HashMap<>();
			properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
			properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
			em.setJpaPropertyMap(properties);
			return em;
		}

//		@Bean
//		@ConfigurationProperties(prefix="spring.second-datasource")
//		public DataSource secondDataSource() {
//			return DataSourceBuilder.create().build();
//		}

		@Bean
		public PlatformTransactionManager secondTransactionManager() {
			JpaTransactionManager transactionManager = new JpaTransactionManager();
			transactionManager.setEntityManagerFactory(secondEntityManager().getObject());
			return transactionManager;
		}
	}
}

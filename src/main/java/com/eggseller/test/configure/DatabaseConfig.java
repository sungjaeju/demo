package com.eggseller.test.configure;

import java.util.Map;
import java.util.Objects;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatabaseConfig {
	/**
	 * DataSources
	 */
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
	
//	public class testDataSourceConfigure {
//		@Primary
//		@Bean(name = "testDataSource")
//		@ConfigurationProperties(prefix = "spring.datasource.test-db")
//		public DataSource testDataSource() {
//			return DataSourceBuilder.create().type(HikariDataSource.class).build();
//		}
//	}

	
//	@EnableJpaRepositories(basePackages = "com.eggseller.test.repository.eggseller")
//	public class eggsellerDataSourceConfigure {
//		private final JpaProperties jpaProperties;
//	    private final HibernateProperties hibernateProperties;
//	    
//	    public eggsellerDataSourceConfigure(JpaProperties jpaProperties, HibernateProperties hibernateProperties) {
//	        this.jpaProperties = jpaProperties;
//	        this.hibernateProperties = hibernateProperties;
//	    }
//		
//		@Bean
//	    @Primary
//	    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
//	       Map<String, Object> properties = hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());
//	
//	        return builder.dataSource(eggsellerDataSource())
//	                .properties(properties)
//	                .packages("com.eggseller.test.entity")
//	                .persistenceUnit("master")
//	                .build();
//	    }
//
//	    @Bean
//	    @Primary
//	    PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder) {
//	        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactory(builder).getObject()));
//	    }
//	}
	
	
	
	/**
	 * JDBC Templates
	 */
	@Primary
	@Bean(name = "testJdbcTemplate")
	public JdbcTemplate testJdbcTemplate(@Qualifier("testDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
	@Bean(name = "eggsellerJdbcTemplate")
	public JdbcTemplate jdbcTemplateEggseller(@Qualifier("eggsellerDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
	/**
	 * TestDB SqlSessionTemplate
	 */
	@Primary
	@MapperScan(value = "com.eggseller.test.repository.test", sqlSessionFactoryRef = "testSqlSessionFactory")
	public class TestSqlSessionTemplate {
		@Bean(name="testSqlSessionFactory")
		public SqlSessionFactory testSqlSessionFactory(@Qualifier("testDataSource") DataSource dataSource) throws Exception {
			SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
			sessionFactory.setDataSource(dataSource);
			sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/test/*.xml"));
			sessionFactory.setTypeAliasesPackage("com.eggseller.test.model");
			return sessionFactory.getObject();
		}
		
	    @Bean(name="testSqlSessionTemplate")
	    public SqlSessionTemplate testSqlSessionTemplate (
	            @Qualifier("testSqlSessionFactory") SqlSessionFactory sqlSessinFactory) throws Exception {
	        return new SqlSessionTemplate(sqlSessinFactory);
	    }
	}
	
	/**
	 * EggsellerDB SqlSessionTemplate
	 */
	@MapperScan(value = "com.eggseller.test.repository.eggseller", sqlSessionFactoryRef = "eggsellerSqlSessionFactory")
	public class EggsellerSqlSessionTemplate {
		@Bean(name="eggsellerSqlSessionFactory")
		public SqlSessionFactory eggsellerSqlSessionFactory(@Qualifier("eggsellerDataSource") DataSource dataSource, ApplicationContext applicationContext) throws Exception {
			SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
			sessionFactory.setDataSource(dataSource);
			//sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/eggseller/*.xml"));
			sessionFactory.setMapperLocations(applicationContext.getResources("classpath:mybatis/eggseller/*.xml"));
			sessionFactory.setTypeAliasesPackage("com.eggseller.test.model");
			return sessionFactory.getObject();
		}
		
	    @Bean(name="eggsellerSqlSessionTemplate")
	    public SqlSessionTemplate eggsellerSqlSessionTemplate(
	            @Qualifier("eggsellerSqlSessionFactory") SqlSessionFactory sqlSessinFactory) throws Exception {
	        return new SqlSessionTemplate(sqlSessinFactory);
	    }		
	    
	    @Bean(name="eggsellerSqlSessionTransactionManager")
	    public DataSourceTransactionManager eggsellerSqlSessionTransactionManager(@Qualifier("eggsellerDataSource") DataSource dataSource) {
	        return new DataSourceTransactionManager(dataSource);
	    }
	}
	

}

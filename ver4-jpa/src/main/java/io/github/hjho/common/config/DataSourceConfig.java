package io.github.hjho.common.config;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jpa.autoconfigure.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableJpaRepositories(
		basePackages = { "io.github.hjho.jpa" },
		entityManagerFactoryRef = "entityManagerFactory",
		transactionManagerRef = "transactionManager"
	)
@EnableTransactionManagement
public class DataSourceConfig {
	
	// document: https://docs.spring.io/spring-data/jpa/reference/repositories/create-instances.html

	// 스프링 5.x 버전부터 빈 등록 시 public 이 아니어도(package-private 등) 동작하므로, 굳이 public 을 명시하지 않아도 됩니다.
	// 기능적인 오류가 아닌 코드 스타일 및 관례에 대한 권장 사항입니다.
	
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	@Bean HikariDataSource dataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}
	
	
	@Bean LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaProperties jpaProperties) {
		
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setDatabase(jpaProperties.getDatabase());
		vendorAdapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
		vendorAdapter.setGenerateDdl(jpaProperties.isGenerateDdl());
		vendorAdapter.setShowSql(jpaProperties.isShowSql());
		// jpaProperties.getOpenInView();
		
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("io.github.hjho.jpa.model");
		factory.setDataSource(dataSource);
		factory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		factory.setPersistenceUnitName("studyjpa");
		factory.setJpaPropertyMap(jpaProperties.getProperties());

		log.debug("## JPA_PROPERTIES: {}", jpaProperties.getProperties());
		// ## JPA_PROPERTIES: {hibernate.format_sql=true, hibernate.physical_naming_strategy=org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy, hibernate.hbm2ddl.auto=validate}
		return factory;
	}

	/*
	@Bean JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }*/
	
	
	@Bean PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory);
		return txManager;
	}
	
}

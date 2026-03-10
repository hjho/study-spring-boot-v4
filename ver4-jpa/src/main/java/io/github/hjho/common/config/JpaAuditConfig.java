package io.github.hjho.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.github.hjho.common.provider.AuditorAwareImpl;

@Configuration(proxyBeanMethods = false)
@EnableJpaAuditing
public class JpaAuditConfig {

	/**
	 * @EnableJpaAuditing 를 사용하면 아래 어노테이션을 사용할 수 있다.
	 * 
	 * @CreatedDate
	 * @LastModifiedDate
	 *     - @EntityListeners(AuditingEntityListener.class)
	 *     - 사용하고자 하는 엔티티 클래스에 붙여줌.
	 * 
	 * @CreatedBy
	 * @LastModifiedBy
	 *     - @Bean auditorProvider() 를 만들어서 주입.
	 */
	@Bean AuditorAware<String> auditorProvider() {
		return new AuditorAwareImpl();
	}
	
}

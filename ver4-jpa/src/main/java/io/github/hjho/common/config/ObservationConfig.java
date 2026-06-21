package io.github.hjho.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;

@Configuration(proxyBeanMethods = false)
public class ObservationConfig {

	/**
	 * ObservedAspect 를 사용하면 @Observed 어노테이션이 작동
	 */
	@Bean ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
        return new ObservedAspect(observationRegistry);
    }
	
}

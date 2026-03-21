package io.github.hjho.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Capability;
import feign.Logger;
import feign.RequestInterceptor;
import io.github.hjho.common.component.FeignCapability;
import io.github.hjho.common.component.FeignHeaderInterceptor;

@Configuration(proxyBeanMethods = false)
public class FeignConfig {

	/*
	@Bean Logger logger() {
		return new FeignLogger();
	} */
	
	
	/**
	 * NONE   : 로깅을 하지 않음.
	 * BASIC  : Request Method, URL, REsponse Status, 실행 시간만 기록.
	 * HEADERS: BASIC 데이터, Request/Response Header 기록.
	 * FULL   : HEADERS 데이터, Request/Response Body, 메타데이터 전부 기록.
	 */
	@Bean Logger.Level feignLoggerLevel() {
		return Logger.Level.NONE;
	}
	
	
	@Bean RequestInterceptor requestInterceptor() {
		return new FeignHeaderInterceptor();
	}
	
    @Bean Capability feignCapability() {
        return new FeignCapability();
    }

}

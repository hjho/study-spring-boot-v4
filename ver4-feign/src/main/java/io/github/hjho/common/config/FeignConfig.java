package io.github.hjho.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Capability;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import io.github.hjho.common.component.FeignCapability;
import io.github.hjho.common.component.FeignErrorDecoder;
import io.github.hjho.common.component.FeignHeaderInterceptor;

@Configuration(proxyBeanMethods = false)
public class FeignConfig {
	
	/* 로거 설정.
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
	
	/* FEIGN 공통 헤더 주입 인터셉터. */
	@Bean RequestInterceptor requestInterceptor() {
		return new FeignHeaderInterceptor();
	}
	
	/* FEIGN Request, Response, Error 인터셉터. */
	@Bean Capability feignCapability() {
		return new FeignCapability();
	}
	
	/* 타임아웃 설정. (ConnectTimeout, ReadTimeout, FollowRedirects)
	@Bean Request.Options options() {
		return new Request.Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true);
	} */
	
	/* 재시도 설정 (기본은 재시도 안 함)
	// 특정 상황(네트워크 오류 등)에서만 재시도
	// period: 100ms(시작 간격), maxPeriod: 1000ms(최대 간격), maxAttempts: 5(최대 시도 횟수, 1회 호출 + 4회 재시도)
	// 100ms 간격으로 시작해서 최대 1초 간격으로, 최대 5번 시도
	@Bean Retryer retryer() {
		return new Retryer.Default(100, SECONDS.toMillis(1), 5);
	} */
	
	/* 에러 디코더 (Retry 연동) */
	@Bean ErrorDecoder errorDecoder() {
		return new FeignErrorDecoder();
	}
	
	/* 디코더 필요시.
	@Bean Decoder decoder() {
		return new CsvDecoder();
	} */
	
}

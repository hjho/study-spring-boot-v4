package io.github.hjho.common.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.github.hjho.common.component.ClientInfoArgumentResolver;
import io.github.hjho.common.component.WebMvcInterceptor;
import ua_parser.Parser;

@Configuration(proxyBeanMethods = false)
public class WebMvcConfig implements WebMvcConfigurer {
	
	
	@Bean Parser uaParser() {
		return new Parser();
	}
	
	@Bean ClientInfoArgumentResolver clientInfoArgumentResolver(Parser uaParser) {
		return new ClientInfoArgumentResolver(uaParser);
	}
	
	@Bean WebMvcInterceptor webMvcInterceptor() {
		return new WebMvcInterceptor();
	}
	
	
	
	/** 인터셉터 설정 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(this.webMvcInterceptor())
		.addPathPatterns("/**")            // 모든 경로에 적용
		.excludePathPatterns("/login", "/static/**"); // 특정 경로는 제외
	}
	
	/* 리졸버 설정 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(this.clientInfoArgumentResolver(this.uaParser()));
	}
	
	
	/** 리소스 핸들러 설정 (정적 자원 매핑)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/path/to/uploads/"); // 외부 경로 연결
    } */
	
	
	/** CORS 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://example.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600); // 1시간 동안 프리플라이트 결과 캐싱
    } */
}

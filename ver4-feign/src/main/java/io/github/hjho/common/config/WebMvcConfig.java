package io.github.hjho.common.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.JacksonJsonView;

import io.github.hjho.common.component.ClientInfoArgumentResolver;
import io.github.hjho.common.component.WebMvcInterceptor;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import ua_parser.Parser;

@Configuration(proxyBeanMethods = false)
@EnableWebMvc
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
	
	@Bean JacksonJsonView jsonView() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		SimpleModule timeModule = new SimpleModule();
		timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
		timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
		
		JacksonJsonView jsonView = new JacksonJsonView(JsonMapper.builder().addModule(timeModule).build());
		jsonView.setExtractValueFromSingleKeyModel(false);
		return jsonView;
	}
	
	
	
	/** 인터셉터 설정 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(this.webMvcInterceptor())
				.addPathPatterns("/**")            				// 모든 경로에 적용
				.excludePathPatterns("/login", "/static/**"); 	// 특정 경로는 제외
	}
	
	/* 리졸버 설정 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(this.clientInfoArgumentResolver(this.uaParser()));
	}
	
	/** 리소스 핸들러 설정 (정적 자원 매핑) */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**") 				// 브라우저에서 "/static"을 붙이지 않고 바로 "/js/main.js"처럼 접근하고 싶다면.
				.addResourceLocations("classpath:/static/")
				// .addResourceHandler("/static/**") 	// 브라우저에서 "/static/js/main.js"라고 호출.
				// .setCachePeriod(3600); // 선택사항: 1시간 동안 캐싱
		;
	} 
	
	/** CORS 설정. (Cross-Origin Resource Sharing)
	 * "내가 만든 서버에 다른 도메인(주소)의 웹사이트가 접근해도 되는지 결정하는 보안 규칙"
	 * @CrossOrigin(origins = "http://localhost:3000") // 특정 @RestController 에만 적용할수도 있음.
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")		// 모든 API 경로에 대해
                .allowedOrigins("https://example.com")	// 허용할 도메인
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")	// 허용할 메서드
                .allowedHeaders("*")	// 모든 헤더 허용
                .allowCredentials(true)	// 쿠키/인증정보 허용 시 필수
                .maxAge(3600); 			// 1시간 동안 브라우저가 이 설정을 캐싱
    } */
}

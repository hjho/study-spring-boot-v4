package io.github.hjho.common.config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
public class WebMvcConfig implements WebMvcConfigurer {
	
	
	
	@Bean MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		// 1. 메시지 파일 경로 설정 (src/main/resources/messages/errors_ko.xml 등)
		// 확장자(.xml)는 생략하고 경로만 작성합니다.
		messageSource.setBasenames("classpath:messages/errors");
		// 2. 기본 인코딩 설정 (XML의 경우 UTF-8 권장)
		messageSource.setDefaultEncoding("UTF-8");
		// 3. 메시지 파일 변경 감지 주기 (개발 시 0, 운영 시 -1 또는 큰 값)
		messageSource.setCacheSeconds(0);
		// 4. 없는 키 요청 시 예외 대신 키값 그대로 반환 여부
		messageSource.setUseCodeAsDefaultMessage(false);
		// 5. 기본 로케일 설정 (한국어)
		Locale.setDefault(Locale.KOREAN);
		return messageSource;
	}
	
	
}

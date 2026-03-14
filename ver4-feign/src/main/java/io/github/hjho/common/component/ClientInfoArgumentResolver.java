package io.github.hjho.common.component;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import io.github.hjho.common.model.ClientInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import ua_parser.Client;
import ua_parser.Parser;

@Slf4j
@Component
public class ClientInfoArgumentResolver implements HandlerMethodArgumentResolver {
	
	
	private final Parser uaParser;
	
	public ClientInfoArgumentResolver(Parser uaParser) {
		this.uaParser = uaParser;
	}
	
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		// 파라미터 타입이 ClientInfo인 경우에만 동작.
		return parameter.getParameterType().equals(ClientInfo.class);
	}
	
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		
		boolean useUserAgent = true;
		
		// UA-CH (Client Hints) 우선 추출
		String browser    = clean(request.getHeader("Sec-CH-UA"));
		String browserVer = null; // 상세 버전은 Full-Version-List 헤더 필요
		String os         = clean(request.getHeader("Sec-CH-UA-Platform"));
		String osVer      = clean(request.getHeader("Sec-CH-UA-Platform-Version"));
		String device     = clean(request.getHeader("Sec-CH-UA-Model"));
		String mobileYn   = "?1".equals(clean(request.getHeader("Sec-CH-UA-Mobile"))) ? "Y" : "N";
		
		// User-Agent 파싱
		if (useUserAgent) {
			String uaString = request.getHeader("User-Agent");
			Client client = uaParser.parse(uaString);
			
			browser = client.userAgent.family;
			browserVer = client.userAgent.major;
			os = client.os.family;
			osVer = client.os.major;
			device = client.device.family;
			mobileYn = uaString.toLowerCase().contains("mobile") ? "Y" : "N";
		} 
		
		ClientInfo client = new ClientInfo(browser, browserVer, os, osVer, device, mobileYn);
		
		log.debug("## client_info: {}", client);
		// ## client_info: ClientInfo(browser=Chrome, browserVersion=146, os=Windows, osVersion=10, device=Other, mobileYn=N)
		// ## client_info: ClientInfo(browser=Chromium;v=146, Not-A.Brand;v=24, Google Chrome;v=146, browserVersion=null, os=Windows, osVersion=, device=, mobileYn=N)
		return client;
	}
	
	// 헤더 값의 큰따옴표 제거용 유틸
    private String clean(String value) {
        return (value != null) ? value.replace("\"", "") : "";
    }
    
}


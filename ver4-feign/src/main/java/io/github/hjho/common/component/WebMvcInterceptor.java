package io.github.hjho.common.component;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.exceptions.TemplateProcessingException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebMvcInterceptor implements HandlerInterceptor {


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String path = StringUtils.defaultString(request.getServletPath());
		if(path.endsWith("com.chrome.devtools.json")) {
			return false;
		}

		String headerValue = request.getHeader("x-requested-with");
		if(headerValue != null && "XMLHttpRequest".equals(headerValue)) {
			request.setAttribute("ajaxYn", "Y");
		} else {
			request.setAttribute("ajaxYn", "N");
		}
		printHeader(request);
		
		log.debug("## preHandle: {}", request.getRequestURL());
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
		String path   = StringUtils.defaultString(request.getServletPath());
		String ajaxYn = Objects.toString(request.getAttribute("ajaxYn"), "N");
		
		if("N".equals(ajaxYn) && path.startsWith("/error")) {
			String type = Objects.toString(request.getAttribute("type"), "none");
			
			log.debug("## postHandle: {}", request.getRequestURL());
			log.debug("## postHandle type: {}", type);
			
			// 공통 에러 응답.
			if(modelAndView != null) {
				modelAndView.addObject("type", type);
			}
		}
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
		if(ex != null) {
			log.error("## afterCompletion: {}", request.getRequestURL());
			
			if(ex instanceof TemplateProcessingException) {
				TemplateProcessingException exception = (TemplateProcessingException) ex;
				log.error("## class  : {}", ex.getClass().getName());
				log.error("## message: {}", exception.getMessage());
				
				request.setAttribute("type", "template");
			} else {
				log.error("## class  : {}", ex.getClass().getName());
				log.error("## message: {}", ex.getMessage());
			}
		}
	}
	
	/**
	 * PRINT HEADER 
	 */
	protected void printHeader(HttpServletRequest request) {
		MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<String, String>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) {
			String headerName  = headerNames.nextElement();
			String headerValue = request.getHeader(headerName);
			
			headerMap.add(headerName, headerValue);
		}
		log.debug("[HTTP] [HEADER ]: {}", headerMap);
	}

	/**
	 * PRINT COOKIE 
	 */
	protected void printCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		
		if(cookies != null) {
			Map<String, String> cookieMap = new LinkedHashMap<String, String>();
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie.getValue());
			}
//			log.debug("[HTTP] [COOKIE ]: {}", ObjectUtil.toJson(cookieMap));
		}
	}
	
	/**
	 * PRINT SESSION 
	 */
	protected void printSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Enumeration<String> sessionNames = session.getAttributeNames();
		Map<String, String> sessionMap = new LinkedHashMap<String, String>();
		sessionMap.put("session-id", session.getId());
//		sessionMap.put("creation-time", DateUtil.longToDateFormat(session.getCreationTime(), DateUtil.DTM_23));
//		sessionMap.put("last-accessed-time", DateUtil.longToDateFormat(session.getLastAccessedTime(), DateUtil.DTM_23));
		sessionMap.put("destroy-time", session.getMaxInactiveInterval() + "초");
		sessionMap.put("destroy-set-time", session.getMaxInactiveInterval() / 60 + "분");
		while(sessionNames.hasMoreElements()) {
			String sessionName = sessionNames.nextElement();
			Object sessionObj  = session.getAttribute(sessionName);
			sessionMap.put(sessionName, String.valueOf(sessionObj));
		}
//		log.debug("[HTTP] [SESSION]: {}", ObjectUtil.toJson(sessionMap));
	}
	
// [HTTP] [HEADER ]: {Host=[localhost:18080], Connection=[keep-alive], sec-ch-ua-platform=["Windows"], User-Agent=[Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/147.0.0.0 Safari/537.36], sec-ch-ua=["Google Chrome";v="147", "Not.A/Brand";v="8", "Chromium";v="147"], sec-ch-ua-mobile=[?0], Accept=[text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7], Upgrade-Insecure-Requests=[1], Sec-Fetch-Site=[none], Sec-Fetch-Mode=[navigate], Sec-Fetch-User=[?1], Sec-Fetch-Dest=[document], Accept-Encoding=[gzip, deflate, br, zstd], Accept-Language=[ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7]}
// [HTTP] [HEADER ]: {Host=[localhost:18080], Connection=[keep-alive], sec-ch-ua-platform=["Windows"], User-Agent=[Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/147.0.0.0 Safari/537.36], sec-ch-ua=["Google Chrome";v="147", "Not.A/Brand";v="8", "Chromium";v="147"], sec-ch-ua-mobile=[?0], Accept=[image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8], Sec-Fetch-Site=[same-origin], Sec-Fetch-Mode=[no-cors], Sec-Fetch-Dest=[image], Referer=[http://localhost:18080/example/test/call], Accept-Encoding=[gzip, deflate, br, zstd], Accept-Language=[ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7]}
	
}

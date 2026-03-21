package io.github.hjho.common.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import feign.Request;
import feign.Request.HttpMethod;
import feign.Response;
import feign.Util;

public class FeignLogUtils {
	
	
	/**
	 * 특정 헤더 값 가져오기 유틸리티
	 */
	public static String getHeader(Request request, String headerName) {
		Collection<String> values = request.headers().get(headerName);
		if (values == null || values.isEmpty()) return null;
		return values.iterator().next();
	}
	
	
	/**
	 * URL의 쿼리 스트링과 Form Body 파라미터를 통합하여 추출
	 */
	public static String getRequestData(Request request) {
		StringBuilder params = new StringBuilder();
		
		HttpMethod method = request.httpMethod();
		
		if(method.equals(HttpMethod.GET)) {
			
		}
		
		// 1. Content-Type 확인 후 Form 데이터인 경우 바디 파싱
		String contentType = getHeader(request, "Content-Type");
		
		
		// 1. URL에서 쿼리 파라미터 추출 (GET, POST 공통)
		// 예: http://api.com/users?id=123&name=test -> id=123&name=test 추출
		String url = request.url();
		if (url.contains("?")) {
			params.append("[Query] ").append(url.substring(url.indexOf("?") + 1));
		}
		
		
		
		if (contentType != null && contentType.contains("application/x-www-form-urlencoded")) {
			String body = getRequestBody(request);
			if (!body.isEmpty()) {
				if (params.length() > 0) params.append(" | ");
				params.append("[Form] ").append(body);
			}
		}
		
		return params.toString();
	}
	
	/**
	 * Request Parameter 추출.
	 */
	public static String getRequestParam(Request request) {
		StringBuilder params = new StringBuilder();
		
		String url = request.url();
		if (url.contains("?")) {
			params.append(url.substring(url.indexOf("?") + 1));
			
		} else {
			String body = getRequestBody(request);
			if (!body.isEmpty()) {
				params.append(body);
			}
		}
		
		return params.toString();
	}
	
	/**
	 * Request Body 추출.
	 */
	public static String getRequestBody(Request request) {
		if (request.body() == null) return "";
		return new String(request.body(), StandardCharsets.UTF_8);
	}
	
	/**
	 * Response Body 추출
	 */
	public static String getResponseBody(Response response) throws IOException {
		if(response.body() == null) return "";
		
		byte[] bodyData = Util.toByteArray(response.body().asInputStream());
		
		return new String(bodyData, StandardCharsets.UTF_8);
	}
	
}

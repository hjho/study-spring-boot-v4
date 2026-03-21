package io.github.hjho.common.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import feign.Request;
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

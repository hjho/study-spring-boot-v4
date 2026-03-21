package io.github.hjho.common.component;

import java.io.IOException;

import feign.Capability;
import feign.Client;
import feign.Request;
import feign.Response;
import feign.Util;
import io.github.hjho.common.model.FeignLogInfo;
import io.github.hjho.common.util.FeignLogUtils;
import io.github.hjho.common.util.ModelUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignCapability implements Capability {
	
	
	@Override
	public Client enrich(Client client) {
		return (request, options) -> {
			long startTime = System.currentTimeMillis();
			Response response;
			
			// 1. 요청 수집
			intercept("REQUEST_FEIGN", request, null, 0, null);
			
			try {
				// 2. 요청 실행
				response = client.execute(request, options);
			} catch (Exception e) {
				// 에러 발생 시 수집 로직 (Elastic 전송 등)
				intercept("ERROR_FEIGN", request, null, startTime, e);
				throw e;
			}
			
			// 응답 바디 Rebuffer (데이터 수집을 위해 필요)
			byte[] bodyData = null;
			if (response.body() != null) {
				bodyData = Util.toByteArray(response.body().asInputStream());
				response = response.toBuilder().body(bodyData).build();
			}
			
			// 4. 정상 응답 수집
			intercept("RESPONSE_FEIGN", request, response, startTime, null);
			return response;
		};
	}
	
	/**
	 * @param type "REQUEST_FEIGN"|"RESPONSE_FEIGN"|"ERROR_FEIGN"
	 * @throws IOException 
	 */
	private void intercept(String type, Request request, Response response, long startTime, Exception e) throws IOException {
		String traceId  = "";
		String param    = null;
		String reqBody  = null;
		String resBody  = null;
		String message  = null;
		long   duration = 0;
		int    status   = 0;
		
		if(type.startsWith("REQUEST")) {
			String contentType = FeignLogUtils.getHeader(request, "Content-Type");
			
			if(contentType == null || contentType.contains("x-www-form-urlencoded")) {
				param = FeignLogUtils.getRequestParam(request);
		    	
		    } else if (contentType.contains("json")) {
		    	reqBody = FeignLogUtils.getRequestBody(request);
		    }
		
		} else {
			duration = System.currentTimeMillis() - startTime;
			status = response.status();
		}
		
		if(type.startsWith("RESPONSE")) {
			resBody = FeignLogUtils.getResponseBody(response);
		}
		
		if(type.startsWith("ERROR")) {
			message = (e != null) ? e.getMessage() : "";
		}
		
	    FeignLogInfo feignLogInfo = 
	    		FeignLogInfo.builder()
	    			.type(type)
					.traceId(traceId)
					.clientName(request.requestTemplate().feignTarget().name())
					.method(request.httpMethod().name())
					.url(request.url())
					.requestParam(param)
					.status(status)
					.duration(duration)
					.requestBody(reqBody)
					.responseBody(resBody)
					.errorMessage(message)
				.build();
	    
	    log.info("##### [{}]: {}", type, ModelUtils.toJsonString(feignLogInfo));
	}
	
}

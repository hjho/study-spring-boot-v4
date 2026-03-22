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
			
			// 1. 요청 수집.
			intercept("REQUEST_FEIGN", request, null, 0, null);
			
			try {
				// 2. 요청 실행.
				response = client.execute(request, options);
			} catch (Exception e) {
				// 5. 에러 수집.
				// 서버에서 발생한 Exception (Bad Request)는 잡지 않음.
				intercept("ERROR_FEIGN", request, null, startTime, e);
				throw e;
			}
			
			// 3. 응답 바디.  (재사용 가능하도록 변경)
			if (response.body() != null) {
				byte[] bodyData = Util.toByteArray(response.body().asInputStream());
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
					// 기본 내용.
					.type(type)
					.traceId(traceId)
					.clientName(request.requestTemplate().feignTarget().name())
					// 요청 내용.
					.method(request.httpMethod().name())
					.url(request.url())
					.requestParam(param)
					.requestBody(reqBody)
					// 응답 내용.
					.status(status)
					.duration(duration)
					.responseBody(resBody)
					.errorMessage(message)
				.build();
		
		log.info("##### [{}]: {}", type, ModelUtils.toJsonString(feignLogInfo));
		// ##### [REQUEST_FEIGN]: {"type":"REQUEST_FEIGN","traceId":"","clientName":"thread-sleep","method":"GET","url":"http://localhost:8090/example/test/thread-sleeps?name=heopanman","duration":0,"status":0,"requestParam":"name=heopanman"}
		// ##### [RESPONSE_FEIGN]: {"type":"RESPONSE_FEIGN","traceId":"","clientName":"thread-sleep","method":"GET","url":"http://localhost:8090/example/test/thread-sleeps?name=heopanman","duration":5004,"status":200,"responseBody":{"seconds":"5","message":"seconds sleep,,,"}}
	}
	
}

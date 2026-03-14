package io.github.hjho.common.component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignLogger extends Logger {

	@Override
	protected void log(String configKey, String format, Object... args) {
		// 기본 log.
		log.debug(String.format(methodTag(configKey) + format, args));
	}
	
	@Override
	protected void logRequest(String configKey, Level logLevel, Request request) {
		
		String data = request.body() != null ? new String(request.body(), StandardCharsets.UTF_8) : "None"; 
		
		String url = request.url();
		if(url.contains("?")) {
			data = url.split("\\?")[1];
		}
		
		log.info("\n# [Feign Request]\n# {} \n#  - request: ({}) {} \n#  - header : {} \n#  - data   : {}", configKey, request.httpMethod(), request.url(), request.headers(), data);
		// # [Feign Request]
		// # ThreadSleepClient#threadsleep() 
		// #  - request: (GET) http://localhost:8090/example/test/thread-sleeps 
		// #  - header : {request-test=[study-feign]} 
		// #  - data   : None
		
		super.logRequest(configKey, logLevel, request);
	}
	
	@Override
	protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime)
			throws IOException {
		
		String data = "None";
		
		if(response.body() != null && !(response.status() == 204 || response.status() == 205)) {
			byte[] bodyData = Util.toByteArray(response.body().asInputStream());
			data = new String(bodyData, StandardCharsets.UTF_8);
			
			// 읽은 바디데이터를 다시 Response 객체에 넣어야 비즈니스 로직(Decoder)에서 읽을 수 있음.
			response = response.toBuilder().body(bodyData).build();
		}
		
		log.info("\n# [Feign Response]\n# {} \n#  - status: {} ({}ms) \n#  - data  : {}", configKey, response.status(), elapsedTime, data);
		// # [Feign Response]
		// # ThreadSleepClient#threadsleep() 
		// #  - status: 200 (5003ms) 
		// #  - data  : 5 seconds sleep,,,
		
		return super.logAndRebufferResponse(configKey, logLevel, response, elapsedTime);
	}

}

/*
Logger.Level.FULL;
[AssignPlayerClient#findQueryDslDynamic] ---> GET http://localhost:8080/assign-player/find/dynamic?teamCode=T01 HTTP/1.1
[AssignPlayerClient#findQueryDslDynamic] X-Request-Source: study-feign
[AssignPlayerClient#findQueryDslDynamic] ---> END HTTP (0-byte body)
[AssignPlayerClient#findQueryDslDynamic] <--- HTTP/1.1 200 (23ms)
[AssignPlayerClient#findQueryDslDynamic] connection: keep-alive
[AssignPlayerClient#findQueryDslDynamic] content-type: application/json
[AssignPlayerClient#findQueryDslDynamic] date: Wed, 04 Mar 2026 13:10:37 GMT
[AssignPlayerClient#findQueryDslDynamic] keep-alive: timeout=60
[AssignPlayerClient#findQueryDslDynamic] transfer-encoding: chunked
[AssignPlayerClient#findQueryDslDynamic] 
[AssignPlayerClient#findQueryDslDynamic] {"code":"0000","data":[{"team":{"code":"T01","name":"땅","info":{"ownerName":"허육군","mobile":"01011112222","address":"영토"}},"player":{"code":"P01","name":"호랑이","age":33,"created":"2026.02.16 17:21:03","updated":null,"card":{"cardNo":"1357","brandNm":"신한카드"},"position":[{"code":"F","career":1}]},"backNo":17,"nickName":"관악산 호랑이"},{"team":{"code":"T01","name":"땅","info":{"ownerName":"허육군","mobile":"01011112222","address":"영토"}},"player":{"code":"P05","name":"독수리","age":50,"created":"2026.02.16 17:21:03","updated":null,"card":{"cardNo":"4567","brandNm":"기업카드"},"position":[{"code":"D","career":1},{"code":"U","career":23}]},"backNo":99,"nickName":null},{"team":{"code":"T01","name":"땅","info":{"ownerName":"허육군","mobile":"01011112222","address":"영토"}},"player":{"code":"P02","name":"사자","age":55,"created":"2026.02.16 17:21:03","updated":null,"card":{"cardNo":null,"brandNm":null},"position":[{"code":"D","career":10},{"code":"F","career":8}]},"backNo":10,"nickName":"에버랜드 사자"}],"message":"정상 처리 되었습니다."}
[AssignPlayerClient#findQueryDslDynamic] <--- END HTTP (1114-byte body)


Logger.Level.HEADERS;
[AssignPlayerClient#findQueryDslDynamic] ---> GET http://localhost:8080/assign-player/find/dynamic?teamCode=T01 HTTP/1.1
[AssignPlayerClient#findQueryDslDynamic] X-Request-Source: study-feign
[AssignPlayerClient#findQueryDslDynamic] ---> END HTTP (0-byte body)
[AssignPlayerClient#findQueryDslDynamic] <--- HTTP/1.1 200 (24ms)
[AssignPlayerClient#findQueryDslDynamic] connection: keep-alive
[AssignPlayerClient#findQueryDslDynamic] content-type: application/json
[AssignPlayerClient#findQueryDslDynamic] date: Wed, 04 Mar 2026 13:14:43 GMT
[AssignPlayerClient#findQueryDslDynamic] keep-alive: timeout=60
[AssignPlayerClient#findQueryDslDynamic] transfer-encoding: chunked
[AssignPlayerClient#findQueryDslDynamic] <--- END HTTP (1114-byte body)


Logger.Level.BASIC;
[AssignPlayerClient#findQueryDslDynamic] ---> GET http://localhost:8080/assign-player/find/dynamic?teamCode=T01 HTTP/1.1
[AssignPlayerClient#findQueryDslDynamic] <--- HTTP/1.1 200 (23ms)







 */
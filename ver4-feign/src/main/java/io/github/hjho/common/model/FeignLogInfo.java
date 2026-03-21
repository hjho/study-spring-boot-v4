package io.github.hjho.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRawValue;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값은 제외해서 인덱스 용량 절약
public class FeignLogInfo {
	
	// 기본 정보
	private String type;     	 // "REQUEST_FEIGN"|"RESPONSE_FEIGN"|"ERROR_FEIGN"
    private String traceId;      // 서비스 간 추적용 ID
    private String clientName;   // 어떤 FeignClient인지
    private String method;       // GET, POST...
    private String url;          // 호출 URL
    
    // 성능 정보
    private long duration;       // 소요 시간 (ms)
    private int status;          // HTTP 상태 코드
    
    private String requestParam;
    
    @JsonRawValue
    private String requestBody;  // 이미 JSON 문자열인 경우 이중 인스케이프 방지
    
    @JsonRawValue
    private String responseBody;
    
    private String errorMessage; // 에러 발생 시 메시지

}

package io.github.hjho.common.component;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    // Feign의 기본 디코더 (Retry-After 헤더 처리를 내장하고 있음)
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        // 1. 공통 에러 로그 (필요한 정보만 간단히)
        log.error("[Feign Error] Method: {}, Status: {}, URL: {}", methodKey, response.status(), response.request().url());

        return defaultDecoder.decode(methodKey, response);
    }
    
}
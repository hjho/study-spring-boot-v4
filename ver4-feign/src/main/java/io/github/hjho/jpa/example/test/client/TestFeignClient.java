package io.github.hjho.jpa.example.test.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import io.github.hjho.common.model.FeignResponse;
import io.github.hjho.jpa.example.test.model.TestFeignRequest;
import io.github.hjho.jpa.example.test.model.TestFeignResponse;

@FeignClient(name = "test-feign", url = "${project.study-jpa}")
public interface TestFeignClient {

	
	@GetMapping("/example/test/feign/success")
	List<TestFeignResponse> success(@SpringQueryMap TestFeignRequest input);
	
	@GetMapping("/example/test/feign/error-decoder")
	ResponseEntity<TestFeignResponse> errordecoder(@SpringQueryMap TestFeignRequest input);
	
	@PostMapping("/example/test/feign/decoder")
	FeignResponse<List<TestFeignResponse>> decoder(@SpringQueryMap TestFeignRequest input);
	
}

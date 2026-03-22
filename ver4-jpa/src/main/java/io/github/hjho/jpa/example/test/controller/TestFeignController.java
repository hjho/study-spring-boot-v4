package io.github.hjho.jpa.example.test.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.hjho.common.exception.InvalidRequestException;
import io.github.hjho.common.model.FeignResponse;
import io.github.hjho.jpa.example.test.model.TestFeignRequest;
import io.github.hjho.jpa.example.test.model.TestFeignResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/example/test/feign")
public class TestFeignController {
	
	
	/**
	 * FeignErrorDecoder 테스트: 기존의 응답과는 다른 ErrorResponse 응답 일 때.
	 */
	@GetMapping("/success")
	public List<TestFeignResponse> success(@ModelAttribute() TestFeignRequest input) {
		List<TestFeignResponse> list = new ArrayList<>();
		list.add(TestFeignResponse.builder().language("korean").createAt(LocalDateTime.now()).build());
		list.add(TestFeignResponse.builder().language("english").build());
		list.add(TestFeignResponse.builder().language("japanese").build());
		
		return list;
	}
	
	/**
	 * FeignErrorDecoder 테스트: 기존의 응답과는 다른 ErrorResponse 응답 일 때.
	 */
	@GetMapping("/error-decoder")
	public ResponseEntity<TestFeignResponse> errorDecoder(@ModelAttribute TestFeignRequest input) {
		throw new InvalidRequestException("E002");
	}
	
	/**
	 * FeignDecoder 테스트: 일반 responseDto 가 아닌, 200정상인데 응답코드가 9999 일 때.
	 */
	@GetMapping("/decoder")
	public ResponseEntity<FeignResponse<List<TestFeignResponse>>> decoder(@ModelAttribute TestFeignRequest input) {
		String type = input.getType();
		
		FeignResponse<List<TestFeignResponse>> output = new FeignResponse<>();
		if("language".equals(type)) {
			List<TestFeignResponse> list = new ArrayList<>();
			list.add(TestFeignResponse.builder().language("korean").build());
			list.add(TestFeignResponse.builder().language("english").build());
			list.add(TestFeignResponse.builder().language("japanese").build());
			
			output.setCode("0000");
			output.setMessage("정상적으로 처리 되었습니다.");
			output.setData(list);
			
		} else {
			output.setCode("9999");
			output.setMessage("TYPE을 다시 확인해주세요.");
		}
		
		return ResponseEntity.ok(output);
	}
	
}

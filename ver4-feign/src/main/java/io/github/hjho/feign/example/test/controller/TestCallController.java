package io.github.hjho.feign.example.test.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import feign.FeignException;
import io.github.hjho.common.exception.ModelAndViewException;
import io.github.hjho.jpa.example.test.client.TestFeignClient;
import io.github.hjho.jpa.example.test.model.TestFeignRequest;
import io.github.hjho.jpa.example.test.model.TestFeignResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/example/test/call")
@RequiredArgsConstructor
public class TestCallController {
	
	@Qualifier("testFeignClient")
	private final TestFeignClient client;
	
	@GetMapping
	String index() {
		return "/feign/example/test/indexTestCall";
	}
	
	@GetMapping("/error")
	String error() throws Exception {
		throw new Exception("화면 이동중 일으킨 에러"); 
	}
	
	@GetMapping("/error/feign")
	String error_feign() throws Exception {
		ResponseEntity<TestFeignResponse> response = client.errordecoder(TestFeignRequest.builder().type("language").build());
		if(response.getStatusCode().is4xxClientError()) {
			// 호출 안됨.
			log.error("## ERROR: {}", response.getBody());
		}
		return "/feign/example/test/indexTestCall";
	}
	
	@GetMapping("/error/feign/catch")
	String error_feign_catch() throws Exception {
		try {
			client.errordecoder(TestFeignRequest.builder().type("language").build());
		} catch (FeignException e) {
			log.error("## ERROR: {}", e.getMessage());
			// ## ERROR: [400] during [GET] to [http://localhost:18090/example/test/feign/error-decoder?type=language] [TestFeignClient#errordecoder(TestFeignRequest)]: [{"code":"E002","message":"값을 입력해주세요.","status":400}]
		}
		return "/feign/example/test/indexTestCall";
	}
	
	@GetMapping("/error/template")
	String error_template() throws Exception {
		return "/feign/example/test/template_input_error";
	}
	
	/**********************************************************************************/
	
	@GetMapping("/api")
	@ResponseBody ResponseEntity<Map<String, Object>> api() {
		
		Map<String, Object> output = new HashMap<>();
		output.put("time", LocalDateTime.now());
		
		return ResponseEntity.ok(output);
	}
	
	@GetMapping("/api/error")
	@ResponseBody TestFeignResponse api_error() throws Exception {
		ResponseEntity<TestFeignResponse> response = client.errordecoder(TestFeignRequest.builder().type("language").build());
		if(response.getStatusCode().is4xxClientError()) {
			// 호출 안됨.
			log.error("## ERROR: {}", response.getBody());
		}
		return response.getBody();
	}
	
	/**********************************************************************************/
	
	@GetMapping("/json-view")
	ModelAndView jsonView() throws Exception {
		return new ModelAndView("/feign/example/test/indexTestCall");
	}
	
	
	@GetMapping("/error/json-view")
	String error_jsonView() throws Exception {
		throw new ModelAndViewException("화면 이동중 일으킨 에러");
	}
	
	@GetMapping("/api/json-view")
	ModelAndView api_jsonView() {
		ModelAndView mav = new ModelAndView("jsonView");
		
		Map<String, Object> output = new HashMap<>();
		output.put("time", LocalDateTime.now());
		
		mav.addObject(output);
		return mav;
	}
	
	@GetMapping("/api/error/json-view")
	ModelAndView api_error_jsonView() throws Exception {
		throw new ModelAndViewException("API 호출 중 일으킨 에러"); 
	}
	
	
	
}

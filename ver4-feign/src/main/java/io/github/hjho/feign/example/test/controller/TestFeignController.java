package io.github.hjho.feign.example.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import io.github.hjho.common.model.FeignResponse;
import io.github.hjho.jpa.example.test.client.TestFeignClient;
import io.github.hjho.jpa.example.test.model.TestFeignRequest;
import io.github.hjho.jpa.example.test.model.TestFeignResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/example/test/feign")
@RequiredArgsConstructor
public class TestFeignController {
	
	@Qualifier("testFeignClient")
	private final TestFeignClient client;
	
	
	@GetMapping("/success")
	ModelAndView success() {
		
		List<TestFeignResponse> output = client.success(TestFeignRequest.builder().type("language").build());
		
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("data", output);
		return mav;
	}
	
	@GetMapping("/error-decoder")
	@ResponseBody ResponseEntity<TestFeignResponse> errordecoder() {
		return client.errordecoder(TestFeignRequest.builder().type("language").build());
	}
	
	@GetMapping("/decoder")
	@ResponseBody FeignResponse<List<TestFeignResponse>> decoder() {
		return client.decoder(TestFeignRequest.builder().type("language1").build());
	}
	

}

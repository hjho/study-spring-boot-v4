package io.github.hjho.jpa.example.test.controller;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.github.hjho.common.exception.InvalidRequestException;
import io.github.hjho.jpa.example.test.model.ThreadSleepRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/example/test/thread-sleeps")
public class ThreadSleepController {

	
	@GetMapping
	public ResponseEntity<String> threadsleep(HttpServletRequest request) throws InterruptedException {
		
		log.debug("## header: {}", request.getHeader("request-test"));
		
		Thread.sleep(Duration.ofSeconds(5));
		
		return ResponseEntity.ok("5 seconds sleep,,,");
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<?> threadsleep(@ModelAttribute ThreadSleepRequestDto requestDto) throws InterruptedException {
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		log.debug("## header: {}", request.getHeader("request-test"));
		
		if(requestDto.getSeconds() == null || requestDto.getSeconds() == 0 || requestDto.getSeconds() > 30) {
			throw new InvalidRequestException("E002");
		}
		Thread.sleep(Duration.ofSeconds(requestDto.getSeconds()));
		
		return ResponseEntity.ok(requestDto.getSeconds() + " seconds sleep,,,");
	}
	
}

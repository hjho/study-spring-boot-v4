package io.github.hjho.feign.example.test.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.hjho.common.model.ClientInfo;
import io.github.hjho.jpa.example.test.client.ThreadSleepClient;
import io.github.hjho.jpa.example.test.model.ThreadSleepRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/example/code/thread-sleeps")
@RequiredArgsConstructor
public class ThreadSleepController {
	
	
	private final ThreadSleepClient threadSleepClient;
	
	
	@GetMapping
	@ResponseBody ResponseEntity<String> threadsleep(ClientInfo clientInfo) {
		log.debug("### threadsleep: {}", clientInfo);
		// ### threadsleep: ClientInfo(browser=Chrome, browserVersion=146, os=Windows, osVersion=10, device=Other, mobileYn=N)
		
		return threadSleepClient.threadsleep("heopanman");
	}
	
	@GetMapping("/seconds")
	@ResponseBody ResponseEntity<String> threadsleep2() {
		return threadSleepClient.threadsleep2(new ThreadSleepRequestDto(4L));
	}
	
	@GetMapping("/{seconds}")
	@ResponseBody ResponseEntity<String> threadsleep(@PathVariable("seconds") Long seconds) {
		return threadSleepClient.threadsleep(new ThreadSleepRequestDto(seconds));
	}
	

}

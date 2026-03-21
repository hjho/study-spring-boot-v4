package io.github.hjho.jpa.example.test.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.hjho.jpa.example.test.model.ThreadSleepRequestDto;

@FeignClient(name = "thread-sleep", url = "${project.study-jpa}")
public interface ThreadSleepClient {

	
	@GetMapping("/example/test/thread-sleeps")
	ResponseEntity<String> threadsleep(@RequestParam("name") String name);
	
	@PostMapping("/example/test/thread-sleeps/seconds")
	ResponseEntity<String> threadsleep2(@RequestBody ThreadSleepRequestDto requestDto);
	
	@PostMapping(path = "/example/test/thread-sleeps", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	ResponseEntity<String> threadsleep(@SpringQueryMap ThreadSleepRequestDto requestDto);

}

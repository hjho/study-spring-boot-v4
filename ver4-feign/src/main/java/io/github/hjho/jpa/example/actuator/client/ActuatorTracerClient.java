package io.github.hjho.jpa.example.actuator.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import io.github.hjho.jpa.example.actuator.model.ActuatorTracerResponse;

@FeignClient(name = "actuator-tracer", url = "${project.study-jpa}")
public interface ActuatorTracerClient {
	
	@GetMapping("/example/actuator/tracer")
	ResponseEntity<ActuatorTracerResponse> call();
	
}

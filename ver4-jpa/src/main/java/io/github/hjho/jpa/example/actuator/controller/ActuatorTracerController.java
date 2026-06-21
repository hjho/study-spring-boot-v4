package io.github.hjho.jpa.example.actuator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.hjho.jpa.example.actuator.model.ActuatorTracerResponse;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/example/actuator/tracer")
@RequiredArgsConstructor
public class ActuatorTracerController {

	// io.micrometer.tracing.Tracer
	private final Tracer tracer;
	
	@GetMapping
	public ResponseEntity<ActuatorTracerResponse> call() {
		
		String traceId = "";
		String spanId = "";
        
		Span span = tracer.currentSpan();
        if (span != null) {
            traceId = span.context().traceId();
            spanId = span.context().spanId();
        }
        
		log.debug("## traceId: {}, spanId: {}", traceId, spanId);
		// ## traceId: 6a37cebe38c77ace0867802dabdd51c5, spanId: 3c5a0d95598d35c6
		
		ActuatorTracerResponse response = ActuatorTracerResponse.builder().traceId(traceId).spanId(spanId).build();
		
		return ResponseEntity.ok(response);
	}
	
	
}

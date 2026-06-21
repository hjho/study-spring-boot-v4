package io.github.hjho.jpa.example.actuator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.hjho.jpa.example.actuator.model.ActuatorTracerResponse;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
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
	
	private final ObservationRegistry registry;
	
	
	@GetMapping
	public ResponseEntity<ActuatorTracerResponse> call() {
		
		String traceId = "";
		String spanId = "";
		
		Span before = tracer.currentSpan();
		if (before != null) {
			traceId = before.context().traceId();
			spanId = before.context().spanId();
		}
		log.debug("## before traceId: {}, spanId: {}", traceId, spanId);
		
		ActuatorTracerResponse response = ActuatorTracerResponse.builder().traceId(traceId).spanId(spanId).build();
		
		Observation.createNotStarted("my.custom.operation", registry)
			.contextualName("custom-span-name")
			.observe(() -> {
				// 이 안에서 발생하는 로그는 동일한 Trace ID를 공유하며, 별도의 Span으로 측정됩니다.
				Span newSpan = tracer.currentSpan();
				if (newSpan != null) {
					log.debug("##    new traceId: {}, spanId: {}", newSpan.context().traceId(), newSpan.context().spanId());
				}
			});
		
		Span after = tracer.currentSpan();
		if (after != null) {
			log.debug("##  after traceId: {}, spanId: {}", after.context().traceId(), after.context().spanId());
		}
		
		// ## before traceId: 6a37d4f5faf1276be56ebdcc2336e4ed, spanId: b70bfb828b69e41a
		// ##    new traceId: 6a37d4f5faf1276be56ebdcc2336e4ed, spanId: 1cdf86ae80fa5275
		// ##  after traceId: 6a37d4f5faf1276be56ebdcc2336e4ed, spanId: b70bfb828b69e41a
		return ResponseEntity.ok(response);
	}
	
}

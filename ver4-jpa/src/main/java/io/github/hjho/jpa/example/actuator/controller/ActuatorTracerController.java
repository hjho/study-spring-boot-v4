package io.github.hjho.jpa.example.actuator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.hjho.jpa.example.actuator.model.ActuatorTracerResponse;
import io.github.hjho.jpa.example.actuator.service.ActuatorTracerService;
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
	
	private final ActuatorTracerService actuatorTracerService;
	
	
	@GetMapping
	public ResponseEntity<ActuatorTracerResponse> call() {
		
		String traceId = "";
		String spanId = "";
		
		Span before = tracer.currentSpan();
		if (before != null) {
			traceId = before.context().traceId();
			spanId = before.context().spanId();
		}
		log.debug("##   before traceId: {}, spanId: {}", traceId, spanId);
		
		ActuatorTracerResponse response = ActuatorTracerResponse.builder().traceId(traceId).spanId(spanId).build();
		
		Observation.createNotStarted("service.process", registry)
			.contextualName("process-name")
			.observe(() -> {
				// 이 안에서 발생하는 로그는 동일한 Trace ID를 공유하며, 별도의 Span으로 측정됩니다.
				Span newSpan = tracer.currentSpan();
				if (newSpan != null) {
					log.debug("##    inner traceId: {}, spanId: {}", newSpan.context().traceId(), newSpan.context().spanId());
				}
			});
		
		// basic service function call
		ActuatorTracerResponse basic = actuatorTracerService.basic();
		log.debug("##    basic traceId: {}, spanId: {}", basic.getTraceId(), basic.getSpanId());
		
		// observed service function call
		ActuatorTracerResponse outter = actuatorTracerService.observed();
		log.debug("## observed traceId: {}, spanId: {}", outter.getTraceId(), outter.getSpanId());
		
		Span after = tracer.currentSpan();
		if (after != null) {
			log.debug("##    after traceId: {}, spanId: {}", after.context().traceId(), after.context().spanId());
		}
		
		// ##   before traceId: 6a37da297cf14bb17607485098536634, spanId: 30f109bd170247b7
		// ##    inner traceId: 6a37da297cf14bb17607485098536634, spanId: e8dfa46a1d612a15
		// ##    basic traceId: 6a37da297cf14bb17607485098536634, spanId: 30f109bd170247b7
		// ## observed traceId: 6a37da297cf14bb17607485098536634, spanId: 3fdf00a092ec1838
		// ##    after traceId: 6a37da297cf14bb17607485098536634, spanId: 30f109bd170247b7
		return ResponseEntity.ok(response);
	}
	
}

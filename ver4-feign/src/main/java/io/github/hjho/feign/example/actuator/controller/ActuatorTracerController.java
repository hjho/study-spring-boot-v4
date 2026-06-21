package io.github.hjho.feign.example.actuator.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.hjho.jpa.example.actuator.client.ActuatorTracerClient;
import io.github.hjho.jpa.example.actuator.model.ActuatorTracerResponse;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/example/actuator/tracer")
@RequiredArgsConstructor
public class ActuatorTracerController {
	
	// io.micrometer.tracing.Tracer
	private final Tracer tracer;
	
	@Qualifier("actuatorTracerClient")
	private final ActuatorTracerClient client;
	
	
	@GetMapping @ResponseBody Map<String, String> call() {

		String traceId = "";
        String spanId = "";
        
		Span span = tracer.currentSpan();
        if (span != null) {
            traceId = span.context().traceId();
            spanId = span.context().spanId();
        }
		
        ActuatorTracerResponse response = client.call().getBody();
		
		log.debug("## WEB: traceId: {}, spanId: {}", traceId, spanId);
		log.debug("## API: traceId: {}, spanId: {}", response.getTraceId(), response.getSpanId());
		
		Map<String, String> data = Map.of("traceId", traceId, "spanId", spanId);
		
		// ## WEB: traceId: 6a37d4f5faf1276be56ebdcc2336e4ed, spanId: e56ebdcc2336e4ed
		// ## API: traceId: 6a37d4f5faf1276be56ebdcc2336e4ed, spanId: b70bfb828b69e41a
		return data;
	}
	
}

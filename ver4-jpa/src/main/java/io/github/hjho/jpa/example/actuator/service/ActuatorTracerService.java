package io.github.hjho.jpa.example.actuator.service;

import org.springframework.stereotype.Service;

import io.github.hjho.jpa.example.actuator.model.ActuatorTracerResponse;
import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActuatorTracerService {
	
	private final Tracer tracer;
	
	
	public ActuatorTracerResponse basic() {
		String traceId = "";
    	String spanId = "";
    	
    	Span before = tracer.currentSpan();
		if (before != null) {
			traceId = before.context().traceId();
			spanId = before.context().spanId();
		}
		
		return ActuatorTracerResponse.builder().traceId(traceId).spanId(spanId).build();
	}
	
	// name: 메트릭스 이름 (Observation ID)
    // contextualName: Zipkin/Jaeger 등 분산 추적 툴에서 보일 자식 Span ID의 이름
    @Observed(name = "tracer.test", contextualName = "test-outter-span")
	public ActuatorTracerResponse observed() {
    	
    	String traceId = "";
    	String spanId = "";
    	
    	Span before = tracer.currentSpan();
		if (before != null) {
			traceId = before.context().traceId();
			spanId = before.context().spanId();
		}
		
		return ActuatorTracerResponse.builder().traceId(traceId).spanId(spanId).build();
	}

}

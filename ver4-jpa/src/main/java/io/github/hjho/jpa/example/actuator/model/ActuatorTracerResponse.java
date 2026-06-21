package io.github.hjho.jpa.example.actuator.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActuatorTracerResponse {

	private String traceId;
	
	private String spanId;
	
}

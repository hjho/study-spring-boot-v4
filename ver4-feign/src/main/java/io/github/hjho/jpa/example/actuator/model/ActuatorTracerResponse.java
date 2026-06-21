package io.github.hjho.jpa.example.actuator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActuatorTracerResponse {

	private String traceId;
	
	private String spanId;
	
}
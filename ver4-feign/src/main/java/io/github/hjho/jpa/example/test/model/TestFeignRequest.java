package io.github.hjho.jpa.example.test.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestFeignRequest {

	private String type;
	
}

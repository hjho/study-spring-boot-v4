package io.github.hjho.jpa.example.test.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestFeignResponse {
	
	private String language;
	
	private LocalDateTime createAt;

}

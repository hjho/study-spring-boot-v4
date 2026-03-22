package io.github.hjho.jpa.example.test.model;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TestFeignResponse {
	
	private String language;
	
	private LocalDateTime createAt;

}

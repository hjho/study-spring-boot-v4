package io.github.hjho.jpa.example.code.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SelfKeyResponseDto {
	
	private String id;
	
	private String title;
	
	private String content;

	private LocalDateTime createdDate;
	
	private LocalDateTime lastModifiedDate;
	
}

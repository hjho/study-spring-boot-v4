package io.github.hjho.jpa.example.code.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoKeyResponseDto {
	
	private Long   id;
	
	private String title;
	
	private String content;

}

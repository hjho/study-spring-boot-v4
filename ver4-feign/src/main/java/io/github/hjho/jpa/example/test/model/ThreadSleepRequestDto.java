package io.github.hjho.jpa.example.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ThreadSleepRequestDto {
	
	private Long seconds;

}

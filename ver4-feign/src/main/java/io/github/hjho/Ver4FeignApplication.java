package io.github.hjho;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableAutoConfiguration
public class Ver4FeignApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ver4FeignApplication.class, args);
	}

}

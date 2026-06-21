package io.github.hjho.jpa.example.encrypt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.hjho.jpa.example.encrypt.service.EncryptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/example/encrypt/jasypt")
public class EncryptJasyptController {
	
	private final EncryptService encryptService;
	
	
	@GetMapping("/encrypt")
	public ResponseEntity<String> encrypt(@RequestParam String original) {
		
		String encrypted = encryptService.encryptJasypt(original);
		
		return ResponseEntity.ok("ENC(" + encrypted + ")");
	}
	
	@GetMapping("/decrypt")
	public ResponseEntity<String> decrypt(@RequestParam String encrypted) {
		
		String original = encryptService.decryptJasypt(encrypted);
		
		return ResponseEntity.ok(original);
	}
	
}

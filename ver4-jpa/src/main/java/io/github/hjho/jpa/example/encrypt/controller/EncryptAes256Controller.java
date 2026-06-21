package io.github.hjho.jpa.example.encrypt.controller;

import java.security.NoSuchAlgorithmException;

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
@RequestMapping("/example/encrypt/aes256")
public class EncryptAes256Controller {
	
	private final EncryptService encryptService;
	
	
	@GetMapping("/generate-key")
	public ResponseEntity<String> generateKey() throws NoSuchAlgorithmException {
		
        String key = encryptService.generateKey();
        
		return ResponseEntity.ok(key);
	}
	
	@GetMapping("/encrypt")
	public ResponseEntity<String> encrypt(@RequestParam String encrypted) {
		
		String original = encryptService.encryptAes256(encrypted);
		
		return ResponseEntity.ok(original);
	}
	
	@GetMapping("/decrypt")
	public ResponseEntity<String> decrypt(@RequestParam String encrypted) {
		
		String original = encryptService.decryptAes256(encrypted);
		
		return ResponseEntity.ok(original);
	}
	
}

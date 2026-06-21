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
@RequestMapping("/example/encrypt/sha256")
public class EncryptSha256Controller {
	
	private final EncryptService encryptService;
	
	@GetMapping("/generate-salt")
	public ResponseEntity<String> generateSalt() throws NoSuchAlgorithmException {
		
        String key = encryptService.generateSalt();
        
		return ResponseEntity.ok(key);
	}
	
	@GetMapping("/encrypt")
	public ResponseEntity<String> encrypt(@RequestParam String encrypted) {
		
		String original = encryptService.encryptSha256(encrypted);
		
		return ResponseEntity.ok(original);
	}
	
}

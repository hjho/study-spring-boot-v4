package io.github.hjho.jpa.example.crypto.controller;

import java.security.NoSuchAlgorithmException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.hjho.jpa.example.crypto.service.CryptoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/example/crypto/cipher")
public class CryptoCipherController {
	
	private final CryptoService cryptoService;
	
	
	@GetMapping("/generate-key")
	public ResponseEntity<String> generateKey() throws NoSuchAlgorithmException {
		
        String key = cryptoService.generateKey();
        
		return ResponseEntity.ok(key);
	}
	
	@GetMapping("/encrypt")
	public ResponseEntity<String> encrypt(@RequestParam String original) {
		
		String encrypted = cryptoService.encrypt(original);
		
		return ResponseEntity.ok(encrypted);
	}
	
	@GetMapping("/decrypt")
	public ResponseEntity<String> decrypt(@RequestParam String encrypted) {
		
		String original = cryptoService.decrypt(encrypted);
		
		return ResponseEntity.ok(original);
	}
	
}

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
@RequestMapping("/example/crypto/hash")
public class CryptoHashController {
	
	private final CryptoService cryptoService;
	
	
	@GetMapping("/generate-salt")
	public ResponseEntity<String> generateSalt() throws NoSuchAlgorithmException {
		
        String key = cryptoService.generateSalt();
        
		return ResponseEntity.ok(key);
	}
	
	@GetMapping("/ing")
	public ResponseEntity<String> hashing(@RequestParam String original) {
		
		String hasging = cryptoService.hashing(original);
		
		return ResponseEntity.ok(hasging);
	}
	
}

package io.github.hjho.jpa.example.crypto.controller;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/example/crypto/jasypt")
public class CryptoJasyptController {
	
	private final StringEncryptor jasyptStringEncryptor;
	
	
	/**
	 * JASYPT 암호화. (PBEWithHMACSHA512AndAES_256)
	 */
	@GetMapping("/encrypt")
	public ResponseEntity<String> encrypt(@RequestParam String original) {
		
		String encrypted = jasyptStringEncryptor.encrypt(original);
		
		return ResponseEntity.ok("ENC(" + encrypted + ")");
	}
	
	/**
	 * JASYPT 복호화. (PBEWithHMACSHA512AndAES_256)
	 */
	@GetMapping("/decrypt")
	public ResponseEntity<String> decrypt(@RequestParam String encrypted) {
		
		String original = jasyptStringEncryptor.decrypt(encrypted);
		
		return ResponseEntity.ok(original);
	}
	
}

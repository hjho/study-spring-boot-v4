package io.github.hjho.jpa.example.encrypt.service;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EncryptService {

	private final StringEncryptor jasyptStringEncryptor;
	
	
	public String encryptJasypt(String original) {
		return jasyptStringEncryptor.encrypt(original);
	}
	
	public String decryptJasypt(String encrypted) {
		return jasyptStringEncryptor.decrypt(encrypted);
	}
	
}
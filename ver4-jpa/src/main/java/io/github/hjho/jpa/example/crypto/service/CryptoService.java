package io.github.hjho.jpa.example.crypto.service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CryptoService {
	
	@Value("${crypto.aes256-key}")
	private String cryptoAes256Key;
	
	private static final String AES256_ALGORITHM = "AES/GCM/NoPadding";
	private static final int TAG_LENGTH_BIT = 128; // 인증 태그 길이 (안정성 표준)
	private static final int IV_LENGTH_BYTE = 12;  // GCM 권장 IV 길이
	
	/**
	 * AES_256 시크릿키 생성.
	 */
	public String generateKey() throws NoSuchAlgorithmException {
		
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(256);
		
		SecretKey secretKey = keyGenerator.generateKey();
		
		return Base64.getEncoder().encodeToString(secretKey.getEncoded());
	}
	
	/**
	 * AES_256 암호화 수행. (AES/GCM/NoPadding)
	 */
	public String encrypt(String original) {
		
		if(StringUtils.isEmpty(original)) return null;
		
		try {
			byte[] keyBytes = Base64.getDecoder().decode(cryptoAes256Key);
			
			// 1. 매번 새로운 랜덤 IV 생성 (보안 핵심)
			byte[] iv = new byte[IV_LENGTH_BYTE];
			new SecureRandom().nextBytes(iv);
			
			// 2. Cipher 설정
			Cipher cipher = Cipher.getInstance(AES256_ALGORITHM);
			SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
			GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
			
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
			
			// 3. 암호화 수행
			byte[] cipherText = cipher.doFinal(original.getBytes(StandardCharsets.UTF_8));
			
			// 4. 복호화에 필요한 IV를 암호문 앞에 붙여서 반환 (IV + CipherText)
			byte[] combined = ByteBuffer.allocate(iv.length + cipherText.length).put(iv).put(cipherText).array();
			
			return Base64.getEncoder().encodeToString(combined);
			
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * AES_256 복호화 수행. (AES/GCM/NoPadding)
	 */
	public String decrypt(String encrypted) {
		
		if(StringUtils.isEmpty(encrypted)) return null;
		
		try {
			byte[] keyBytes = Base64.getDecoder().decode(cryptoAes256Key);
			
			byte[] decode = Base64.getDecoder().decode(encrypted);
			
			// 1. 앞부분의 IV와 뒷부분의 암호문 분리
			ByteBuffer bb = ByteBuffer.wrap(decode);
			byte[] iv = new byte[IV_LENGTH_BYTE];
			bb.get(iv);
			byte[] cipherText = new byte[bb.remaining()];
			bb.get(cipherText);
			
			// 2. Cipher 설정
			Cipher cipher = Cipher.getInstance(AES256_ALGORITHM);
			
			SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
			GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
			
			cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
			
			// 3. 복호화 및 데이터 무결성 검증 (태그 확인)
			return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/*******************************************************************************************************/
	
	@Value("${crypto.sha256-salt}")
	private String cryptoSha256Salt;
	
	private static final String SHA256_ALGORITHM = "SHA-256";
	
	/**
	 * SHA_256 솔트 생성.
	 */
	public String generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] saltBytes = new byte[16];
		random.nextBytes(saltBytes);
		
		// Java 15+ 내장 HexFormat 사용 (가장 빠르고 가독성 높음)
		return HexFormat.of().formatHex(saltBytes);
	}
	
	/**
	 * SHA_256 암호화 수행. (SHA-256)
	 *   - 유저의 비밀번호로 사용하진 않느다. (솔트가 고정임)
	 *   - 실무적인 표준: BCrypt
	 *   - 가장 최신 최첨단 표준: Argon2id
	 */
	public String hashing(String original) {
		
		if(StringUtils.isEmpty(original)) return null;
		
		try {
			MessageDigest digest = MessageDigest.getInstance(SHA256_ALGORITHM);
			
			// 솔트와 비밀번호를 결합하여 바이트 배열 생성
			String combined = cryptoSha256Salt + original;
			byte[] encodedHash = digest.digest(combined.getBytes(StandardCharsets.UTF_8));
			
			// 64자리 16진수 문자열로 변환하여 리턴
			return HexFormat.of().formatHex(encodedHash);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
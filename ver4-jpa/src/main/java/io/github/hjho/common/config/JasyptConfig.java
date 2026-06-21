package io.github.hjho.common.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@Configuration(proxyBeanMethods = false)
@EnableEncryptableProperties
public class JasyptConfig {
	// implementation 'com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5'
	
	@Value("${jasypt.encryptor.password}")
	private String password;

	@Bean("jasyptStringEncryptor") StringEncryptor jasyptStringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        
        // 실행 시 외부에서 주입받는 마스터 키
        config.setPassword(password); 
        
        // AES-256 핵심 설정
        config.setAlgorithm("PBEWithHMACSHA512AndAES_256"); 
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        
        // AES 알고리즘 사용 시 IV 생성기는 필수입니다!
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator"); 
        
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }
	
}

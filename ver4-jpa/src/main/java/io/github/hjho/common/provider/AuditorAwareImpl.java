package io.github.hjho.common.provider;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

public class AuditorAwareImpl implements AuditorAware<String> {
	
	/*
	@Override
	public Optional<String> getCurrentAuditor() {
		// Spring Security에서 사용자 아이디 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return Optional.of("Anonymous");
		}
		return Optional.of(authentication.getName());
	}*/
	
	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.of("MY_ID");
		// return Optional.of("CHANGE_ID");
	}
	
}

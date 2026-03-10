package io.github.hjho.common.exception;

public class InvalidRequestException extends BusinessException {
	
	private static final long serialVersionUID = 6235933490098662718L;
	
	
	public InvalidRequestException(String errorCode, Object... args) {
		super(errorCode, args);
	}
	
	public InvalidRequestException(String errorCode) {
		super(errorCode);
	}
	
}

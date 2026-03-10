package io.github.hjho.common.exception;

public class ResourceNotFoundException extends BusinessException {
	
	private static final long serialVersionUID = 2463532645663195464L;
	
	
	public ResourceNotFoundException(String errorCode) {
		super(errorCode);
	}
	
}

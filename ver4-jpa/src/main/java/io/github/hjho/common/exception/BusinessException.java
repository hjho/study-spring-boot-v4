package io.github.hjho.common.exception;

public class BusinessException extends RuntimeException {
	
	private static final long serialVersionUID = -2978624291085852625L;

	
	private final String errorCode;
	
	// 타입 안전성: Object[] 대신 String[]을 쓰셔도 되지만, 
	// 숫자나 날짜 형식을 메시지에서 직접 포맷팅({0, number, #,###})하고 싶다면 Object[]가 더 유리합니다.
	private final Object[] args;
	
	public BusinessException(String errorCode, Object... args) {
		super(errorCode);
		this.errorCode = errorCode;
		this.args = args;
	}
	
	public String getErrorCode() { 
		return errorCode; 
	}
	public Object[] getArgs() { 
		return args; 
	}
	
}

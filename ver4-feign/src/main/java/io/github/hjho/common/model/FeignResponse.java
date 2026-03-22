package io.github.hjho.common.model;

import lombok.Data;

@Data
public class FeignResponse<T> {
	
	public static final String SUCCESS = "0000";
	public static final String ERROR   = "9999";

	// "0000" 정상, "9999" 실패.
	private String code;
	
	private String message;
	
	private T data;
	
	public boolean isSuccess() {
		if(this.code == null) {
			return false;
		}
		return SUCCESS.equals(this.code);
	}
	
}

package io.github.hjho.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientInfo {
	
	private String browser;
	
	private String browserVersion;
	
	private String os;
	
	private String osVersion;
	
	private String device;
	
	private String mobileYn;
	
}

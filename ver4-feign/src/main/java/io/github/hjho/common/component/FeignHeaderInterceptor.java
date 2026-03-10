package io.github.hjho.common.component;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignHeaderInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		
		template.header("request-test", this.getServerName());
		
	}
	
	private String getServerName() {
		return "study-feign";
	}

}

package io.github.hjho.common.component;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import feign.FeignException;
import io.github.hjho.common.exception.ModelAndViewException;
import io.github.hjho.common.model.ErrorResponse;
import io.github.hjho.common.util.ModelUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler {

	private final String defaultMessage = "잠시 후 다시 시도해주세요.";
	
	private final boolean onlyMyStack = true;
	
	
	@ExceptionHandler(ModelAndViewException.class)
	public ModelAndView handlerModelAndViewException(HttpServletRequest request, HttpServletResponse response, ModelAndViewException e) {
		log.error("[[handler handlerModelAndViewException]]");
		ModelAndView mav = new ModelAndView();
		
		String ajaxYn = Objects.toString(request.getAttribute("ajaxYn"), "N");
		
		HttpStatus status = HttpStatus.valueOf(500);
		
		ErrorResponse errorResponse = new ErrorResponse(status.value(), "", "화면 이동 중 장애 발생");
		
		mav.setStatus(status);
		mav.addObject("message", errorResponse);
		if("Y".equals(ajaxYn)) {
			mav.setViewName("jsonView");
		} else {
			mav.setViewName("/error/view");
		}
		return mav;
	}
	
	@ExceptionHandler(FeignException.class)
	public Object handlerFeignException(HttpServletRequest request, HttpServletResponse response, FeignException e, Model model) {
		log.error("[[handler FeignException]]");
		this.printErrorLog(e);
		
		String ajaxYn = Objects.toString(request.getAttribute("ajaxYn"), "N");
		ErrorResponse errorResponse = ModelUtils.toModel(e.contentUTF8(), ErrorResponse.class);
		
		if("Y".equals(ajaxYn)) {
			return ResponseEntity.status(HttpStatus.OK).body(errorResponse);
		} else {
			model.addAttribute("message", errorResponse);
			return "/error/common";
		}
	}
	
	@ExceptionHandler(Exception.class)
	public ModelAndView handlerException(HttpServletRequest request, HttpServletResponse response, Exception e) {
		log.error("[[handler handlerException]]");
		this.printErrorLog(e);
		
		ModelAndView mav = new ModelAndView();
		
		String ajaxYn = Objects.toString(request.getAttribute("ajaxYn"), "N");
		
		HttpStatus status = HttpStatus.valueOf(500);
		
		ErrorResponse errorResponse = new ErrorResponse(status.value(), "", defaultMessage);
		
		mav.setStatus(status);
		mav.addObject("message", errorResponse);
		if("Y".equals(ajaxYn)) {
			mav.setViewName("jsonView");
		} else {
			mav.setViewName("/error/common");
		}
		return mav;
	}
	
	
	private void printErrorLog(Exception e) {
		log.error("[ERR] [CLASS]  : {}", e.getClass());
		log.error("[ERR] [CAUSE]  : {}", ((e.getCause() == null) ? "null" : e.getCause().getMessage()));
		log.error("[ERR] [MESSAGE]: {}",  e.getMessage());
		
		if(onlyMyStack) {
			for(StackTraceElement element : e.getStackTrace()) {
				if(element.getClassName().startsWith("io.github.hjho")) {
					log.error("[ERR] [STACK]  : {} #{}", element.getClassName(), element.getLineNumber());
				}
			}
		} else {
			log.error("[ERR] [STACK]  : ", e);
		}
	}
}

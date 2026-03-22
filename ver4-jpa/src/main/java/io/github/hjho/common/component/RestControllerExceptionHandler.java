package io.github.hjho.common.component;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.hjho.common.exception.InvalidRequestException;
import io.github.hjho.common.exception.ResourceNotFoundException;
import io.github.hjho.common.model.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestControllerExceptionHandler {

	private final MessageSource messageSource;
	
	private final String defaultMessage = "잠시 후 다시 시도해주세요.";
	private final boolean onlyMyStack = true;
	
	
	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<ErrorResponse> handlerInvalidRequestException(InvalidRequestException e, Locale locale) {
		this.printErrorLog(e);
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		String errorCode    = e.getErrorCode();
		String errorMessage = messageSource.getMessage(e.getErrorCode(), e.getArgs(), defaultMessage, locale);
		
		ErrorResponse responseDto = new ErrorResponse(status.value(), errorCode, errorMessage);
		return ResponseEntity.status(status).body(responseDto);
	}
	
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handlerResourceNotFoundException(ResourceNotFoundException e, Locale locale) {
		this.printErrorLog(e);
		HttpStatus status = HttpStatus.NOT_FOUND;
		
		String errorCode    = e.getErrorCode();
		String errorMessage = messageSource.getMessage(errorCode, e.getArgs(), defaultMessage, locale);
		
		ErrorResponse responseDto = new ErrorResponse(status.value(), errorCode, errorMessage);
		return ResponseEntity.status(status).body(responseDto);
	}
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handlerException(Exception e, Locale locale) {
		this.printErrorLog(e);
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		
		ErrorResponse responseDto = new ErrorResponse(status.value(), "", "");
		return ResponseEntity.status(status).body(responseDto);
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

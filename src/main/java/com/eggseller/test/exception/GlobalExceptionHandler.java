package com.eggseller.test.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@Component
public class GlobalExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public String handleException(Exception e) {

		e.printStackTrace();
		StackTraceElement trace = e.getStackTrace()[0];
		log.error(e.getClass().getSimpleName() + "발생 : " + trace.getClassName() + " - L:" + trace.getLineNumber());

		if(e instanceof AccessDeniedException) {
			return "error/403";
		}
		else {
			return "error/500";
		}
	}

	/**
	 * Throw Binding Error
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = { BindException.class, MethodArgumentNotValidException.class })
	public String handleValidationExceptions(BindException e) {
		Map<String, String> errors = new HashMap<>();
		String msg = e.getBindingResult().getFieldError().getDefaultMessage();
		log.info("##### binding msg: {}", msg);
		e.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		log.info("##### binding errors: {}", errors.toString());
//		return errors.toString();
		return null;
	}
	
	//@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(value = { BadCredentialsException.class })
	public String handleBadCredentialsException(BindException e) {
		Map<String, Object> errors = new HashMap<>();
		errors.put("code", HttpStatus.UNAUTHORIZED);
		errors.put("message", "Faild login");
		log.info("##### errors: {}", errors);

		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder
				.getRequestAttributes()).getRequest();
		String uri = request.getRequestURI();
		
		return errors.toString();
	}

}

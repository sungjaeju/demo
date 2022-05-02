package com.eggseller.test.exception;

import com.eggseller.test.util.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	//private final ErrorCode errorCode;

//	public BusinessException(String message) {
//		super(message);
//	}
//	
//	public BusinessException(String message, Throwable exception) {
//		super(message, exception);
//	}
}

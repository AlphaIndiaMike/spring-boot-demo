package com.stag22.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class DefaultExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiError> handleException(
			ResourceNotFoundException e,
			HttpServletRequest request,
			HttpServletResponse response){
		ApiError apiError = new ApiError(
				request.getRequestURI(),
				e.getMessage(),
				HttpStatus.NOT_FOUND.value(),
				LocalDateTime.now()
				);
		
		return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InsufficientAuthenticationException.class)
	public ResponseEntity<ApiError> handleException(
			InsufficientAuthenticationException e,
			HttpServletRequest request,
			HttpServletResponse response){
		ApiError apiError = new ApiError(
				request.getRequestURI(),
				e.getMessage(),
				HttpStatus.FORBIDDEN.value(),
				LocalDateTime.now()
				);
		
		return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleException(
			Exception e,
			HttpServletRequest request,
			HttpServletResponse response){
		ApiError apiError = new ApiError(
				request.getRequestURI(),
				e.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				LocalDateTime.now()
				);
		
		return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

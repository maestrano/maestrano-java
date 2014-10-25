package com.maestrano.exception;

public class ApiConnectionException extends MnoException {
	private static final long serialVersionUID = 1L;
	
	public ApiConnectionException(String message) {
		super(message);
	}
	
	public ApiConnectionException(String message, Throwable e) {
		super(message, e);
	}
}

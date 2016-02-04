package com.maestrano.exception;

import com.maestrano.Maestrano;

/**
 * Exception thrown when Maestrano was not properly configured. See {@linkplain Maestrano#configure()}
 *
 */
public class MnoConfigurationException extends MnoException {
	private static final long serialVersionUID = 1L;

	public MnoConfigurationException(String message) {
		super(message);
	}

	public MnoConfigurationException(String message, Throwable e) {
		super(message, e);
	}
}

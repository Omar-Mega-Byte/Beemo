package com.user.exceptions;

public class BadRequestException extends RuntimeException {
	private static final long serialVersionUID = 20250714001L; // Version: YYYYMMDDNNN

	public BadRequestException(String message) {
		super(message);
	}

	public BadRequestException(String message, Throwable cause) {
		super(message, cause);
	}
	
}

package com.user.exceptions;

public class NotFoundException extends RuntimeException {
	private static final long serialVersionUID = 20250714002L; // Version: YYYYMMDDNNN

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
}

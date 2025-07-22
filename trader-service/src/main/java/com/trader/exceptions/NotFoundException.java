package com.trader.exceptions;

/**
 * Exception thrown when a requested resource cannot be found.
 * 
 * This exception is typically used when trying to access a trader, company,
 * or other entity that doesn't exist in the system. It results in HTTP 404 Not Found.
 */
public class NotFoundException extends RuntimeException {
	private static final long serialVersionUID = 20250722002L; // Version: YYYYMMDDNNN

	/**
	 * Constructs a new NotFoundException with the specified detail message.
	 * 
	 * @param message the detail message explaining what was not found
	 */
	public NotFoundException(String message) {
		super(message);
	}

	/**
	 * Constructs a new NotFoundException with the specified detail message and cause.
	 * 
	 * @param message the detail message explaining what was not found
	 * @param cause the underlying cause of this exception
	 */
	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}

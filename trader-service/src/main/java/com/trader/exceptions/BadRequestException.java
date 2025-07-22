package com.trader.exceptions;

/**
 * Exception thrown when a client request contains invalid data or parameters.
 * 
 * This exception is typically used for validation failures, duplicate data,
 * or when the client sends malformed requests. It results in HTTP 400 Bad Request.
 */
public class BadRequestException extends RuntimeException {
	private static final long serialVersionUID = 20250722001L; // Version: YYYYMMDDNNN

	/**
	 * Constructs a new BadRequestException with the specified detail message.
	 * 
	 * @param message the detail message explaining what went wrong
	 */
	public BadRequestException(String message) {
		super(message);
	}

	/**
	 * Constructs a new BadRequestException with the specified detail message and cause.
	 * 
	 * @param message the detail message explaining what went wrong
	 * @param cause the underlying cause of this exception
	 */
	public BadRequestException(String message, Throwable cause) {
		super(message, cause);
	}
}

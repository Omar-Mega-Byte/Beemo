package com.trader.validator;
import java.util.regex.Pattern;

import com.trader.exceptions.BadRequestException;
import com.trader.model.Trader;

/**
 * Validator class for Trader entity
 * 
 * This class provides comprehensive validation for trader registration,
 * login, and data integrity. It validates both inherited user fields
 * and trader-specific fields like name, phone number, and company.
 */
public class TraderValidator {
	
	private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
	
	// Phone number pattern for international formats
	private static final String PHONE_REGEX = "^[+]?[0-9]{10,15}$";
	private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

	/**
	 * Validates a complete Trader object including all required fields
	 * 
	 * @param trader The trader object to validate
	 * @throws BadRequestException if any validation fails
	 */
	public static void validateTrader(Trader trader) {
		if (trader == null) {
			throw new BadRequestException("Trader cannot be null");
		}
		// Validate inherited user fields
		validateUsername(trader.getUsername());
		validateEmail(trader.getEmail());
		validatePassword(trader.getPassword());
		
		// Validate trader-specific fields
		validateName(trader.getName());
		validatePhoneNumber(trader.getPhoneNumber());
		validateCompany(trader.getCompany());
	}
	
	/**
	 * Validates username according to business rules
	 * 
	 * @param username The username to validate
	 * @throws BadRequestException if validation fails
	 */
	public static void validateUsername(String username) {
		if (username == null || username.trim().isEmpty()) {
			throw new BadRequestException("Username cannot be empty");
		}
		if (username.length() < 3) {
			throw new BadRequestException("Username must be at least 3 characters long");
		}
		if (username.length() > 50) {
			throw new BadRequestException("Username cannot exceed 50 characters");
		}
		if (!username.matches("^[a-zA-Z0-9_]+$")) {
			throw new BadRequestException("Username can only contain letters, numbers, and underscores");
		}
	}
	
	/**
	 * Validates email format and length
	 * 
	 * @param email The email to validate
	 * @throws BadRequestException if validation fails
	 */
	public static void validateEmail(String email) {
		if (email == null || email.trim().isEmpty()) {
			throw new BadRequestException("Email cannot be empty");
		}
		if (!EMAIL_PATTERN.matcher(email).matches()) {
			throw new BadRequestException("Email format is invalid");
		}
		if (email.length() > 100) {
			throw new BadRequestException("Email cannot exceed 100 characters");
		}
	}
	
	/**
	 * Validates password strength and length
	 * 
	 * @param password The password to validate
	 * @throws BadRequestException if validation fails
	 */
	public static void validatePassword(String password) {
		if (password == null || password.isEmpty()) {
			throw new BadRequestException("Password cannot be empty");
		}
		if (password.length() < 6) {
			throw new BadRequestException("Password must be at least 6 characters long");
		}
		if (password.length() > 100) {
			throw new BadRequestException("Password cannot exceed 100 characters");
		}
	}
	
	/**
	 * Validates trader's full name
	 * 
	 * @param name The trader's name to validate
	 * @throws BadRequestException if validation fails
	 */
	public static void validateName(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new BadRequestException("Trader name cannot be empty");
		}
		if (name.length() < 2) {
			throw new BadRequestException("Trader name must be at least 2 characters long");
		}
		if (name.length() > 100) {
			throw new BadRequestException("Trader name cannot exceed 100 characters");
		}
		if (!name.matches("^[a-zA-Z\\s.-]+$")) {
			throw new BadRequestException("Trader name can only contain letters, spaces, dots, and hyphens");
		}
	}
	
	/**
	 * Validates phone number format
	 * 
	 * @param phoneNumber The phone number to validate
	 * @throws BadRequestException if validation fails
	 */
	public static void validatePhoneNumber(String phoneNumber) {
		if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
			throw new BadRequestException("Phone number cannot be empty");
		}
		// Remove spaces and common separators for validation
		String cleanPhone = phoneNumber.replaceAll("[\\s()-]", "");
		if (!PHONE_PATTERN.matcher(cleanPhone).matches()) {
			throw new BadRequestException("Phone number format is invalid. Use format: +1234567890 or 1234567890");
		}
	}
	
	/**
	 * Validates company name
	 * 
	 * @param company The company name to validate
	 * @throws BadRequestException if validation fails
	 */
	public static void validateCompany(String company) {
		if (company == null || company.trim().isEmpty()) {
			throw new BadRequestException("Company name cannot be empty");
		}
		if (company.length() < 2) {
			throw new BadRequestException("Company name must be at least 2 characters long");
		}
		if (company.length() > 150) {
			throw new BadRequestException("Company name cannot exceed 150 characters");
		}
		if (!company.matches("^[a-zA-Z0-9\\s.&,-]+$")) {
			throw new BadRequestException("Company name contains invalid characters");
		}
	}
	
	/**
	 * Validates login credentials for traders
	 * 
	 * @param username The username for login
	 * @param password The password for login
	 * @throws BadRequestException if validation fails
	 */
	public static void validateLoginCredentials(String username, String password) {
		if (username == null || username.trim().isEmpty()) {
			throw new BadRequestException("Username cannot be empty");
		}
		if (password == null || password.isEmpty()) {
			throw new BadRequestException("Password cannot be empty");
		}
	}
}
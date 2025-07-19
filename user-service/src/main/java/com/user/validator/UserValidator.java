package com.user.validator;
import java.util.regex.Pattern;

import com.user.exceptions.BadRequestException;
import com.user.model.User;

public class UserValidator {
	
	private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
	
	public static void validateUser(User user) {
		if (user == null) {
			throw new BadRequestException("User cannot be null");
		}
		validateUsername(user.getUsername());
		validateEmail(user.getEmail());
		validatePassword(user.getPassword());
	}
	
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
	
	public static void validateLoginCredentials(String username, String password) {
		if (username == null || username.trim().isEmpty()) {
			throw new BadRequestException("Username cannot be empty");
		}
		if (password == null || password.isEmpty()) {
			throw new BadRequestException("Password cannot be empty");
		}
	}
}
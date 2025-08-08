package services;

import java.util.regex.Pattern;

/**
 * Handles user input validations during registration.
 */
public class RegisterValidation {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );

    /**
     * Checks if the user is at least 18 years old.
     * @param age user's age
     * @return true if 18 or older
     */
    public static boolean isValidAge(int age) {
        return age >= 18;
    }

    /**
     * Validates if the provided email has correct format.
     * @param email the email to validate
     * @return true if email is valid
     */
    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates that the ID is numeric and exactly 9 digits long.
     * @param id the ID to validate
     * @return true if valid
     */
    public static boolean isValidID(String id) {
        return id.matches("\\d{9}");
    }

    /**
     * Validates if a string is not null or blank.
     * @param value the string to check
     * @return true if not blank
     */
    public static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    public static boolean isAdult(String id) {
        // For example, always return true if ID length is 9 digits
        return id != null && id.matches("\\d{9}");
    }
}

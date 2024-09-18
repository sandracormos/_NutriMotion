package com.example.nutrimotion.security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    /**
     * Validates the format of an email address.
     *
     * @param email The email address to be validated.
     * @return {@code true} if the email is valid, {@code false} otherwise.
     */
    public static boolean isEmailValid(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Validates the format of a name, allowing only alphabetical characters.
     *
     * @param name The name to be validated.
     * @return {@code true} if the name is valid, {@code false} otherwise.
     */
    public static boolean isNameValid(String name) {
        String nameRegex = "^[A-Za-z]+$";
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    /**
     * Validates the format of a password, requiring at least one lowercase letter,
     * one uppercase letter, and one digit.
     *
     * @param password The password to be validated.
     * @return {@code true} if the password is valid, {@code false} otherwise.
     */
    public static boolean isPasswordValid(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1pooproyecto1.services;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

/**
 *
 * @author estebanruiz
 */
public class Validation {
    public static boolean legalAge(LocalDate dateOfBirth) {
        return Period.between(dateOfBirth, LocalDate.now()).getYears() >= 18;
    }

    public static boolean validateEmail(String email) {
        String regex = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return Pattern.matches(regex, email);
    }

    public static boolean validatePassword(String password) {
        // Minimum 8 characters, at least one letter and one number
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        return Pattern.matches(regex, password);
    }

    public static boolean validatePassword2Times(String c1, String c2) {
        return c1 != null && c1.equals(c2);
    }

    public static boolean validateUserName(String userName) {
        // Letters, digits, underscores, between 4 and 20 characters
        String regex = "^[\\w]{4,20}$";
        return Pattern.matches(regex, userName);
    }
}

package com.rashad.bestpractice.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static final String regexEmail = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(regexEmail);

    private static final String regexPassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_])(?=\\S+$).{8,20}$";
    private static final Pattern VALID_PASSWORD_REGEX = Pattern.compile(regexPassword);

    public static boolean checkEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.matches();
    }

    public static boolean checkPassword(String password) {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(password);
        return matcher.matches();
    }

    public static boolean checkPhoneNumber(String phoneNumber) {
        if (!phoneNumber.startsWith("+994") && phoneNumber.length() != 13) return false;
        phoneNumber = phoneNumber.substring(1);
        for (char c : phoneNumber.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }
}



// Gmail special (username+something@gmail.com)
//  "^(?=.{1,64}@)[A-Za-z0-9_-+]+(\\.[A-Za-z0-9_-+]+)*@[^-][A-Za-z0-9-+]+(\\.[A-Za-z0-9-+]+)*(\\.[A-Za-z]{2,})$"

package com.rashad.bestpractice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class OtpUtil {
    private final MessageDigest messageDigest;

    public String generateOtpCode() {
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(1000000);
        return String.format("%06d", num);
    }

    public String hashOtpCode(String rawOtp) {
        byte[] otpBytes = rawOtp.getBytes();
        byte[] hashedBytes = messageDigest.digest(otpBytes);
        return bytesToHex(hashedBytes);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public boolean isOtpCorrect(String userInputOtp, String correctHash) {
        return this.hashOtpCode(userInputOtp).equals(correctHash);
    }

}

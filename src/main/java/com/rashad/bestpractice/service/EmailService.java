package com.rashad.bestpractice.service;

import com.rashad.bestpractice.entity.User;
import com.rashad.bestpractice.exception.ErrorCode;
import com.rashad.bestpractice.exception.NotFoundException;
import com.rashad.bestpractice.exception.WrongOtpException;
import com.rashad.bestpractice.model.request.RegisterRequest;
import com.rashad.bestpractice.repository.UserRepository;
import com.rashad.bestpractice.util.EmailTemplateLoader;
import com.rashad.bestpractice.util.OtpUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final EmailTemplateLoader emailTemplateLoader;
    private final OtpUtil otpUtil;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public void resetUserPassword(String email) throws Exception {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "Bu email ilə istifadəçi tapılmadı"));

        generateOneTimePassword(user);
    }

    public void generateOneTimePassword(User appUser) throws MessagingException, IOException {

        String rawOtp = otpUtil.generateOtpCode();
        String encodedOTP = otpUtil.hashOtpCode(rawOtp);

        appUser.setOneTimePassword(encodedOTP);
        appUser.setOtpRequestedTime(new Timestamp(System.currentTimeMillis()));

        userRepository.save(appUser);

        sendOTPEmail(appUser, rawOtp);
    }

    public void sendOTPEmail(User appUser, String OTP) throws MessagingException, IOException {
        String otpPage = emailTemplateLoader.loadEmailTemplate("otp-page", OTP, appUser.getName());

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.getMimeMessage().setContentLanguage(new String[]{"az"});
        helper.getMimeMessage().setHeader("Content-Type", "text/otpPage; charset=UTF-8");
        helper.setFrom(senderEmail, "OTP Yoxlama Kodu");
        helper.setTo(appUser.getEmail());

        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        String subject = "One Time Password (OTP) Kodu - 5 dəqiqə ərzində bitəcək!";

        // Replace placeholders in the HTML template with the actual values
        otpPage = otpPage.replace("$name", appUser.getName());
        otpPage = otpPage.replace("$otp", OTP);
        otpPage = otpPage.replace("$datetime", dateTime);

        helper.setSubject(subject);
        helper.setText(otpPage, true);

        mailSender.send(message);
    }

    public void sendVenueApplicantEmail(RegisterRequest registerRequest) throws MessagingException, IOException {
        String otpPage = emailTemplateLoader.loadVenueApplicantEmailTemplate("applicant-page", registerRequest);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.getMimeMessage().setContentLanguage(new String[]{"az"});
        helper.getMimeMessage().setHeader("Content-Type", "text/otpPage; charset=UTF-8");
        helper.setFrom(senderEmail, "StadTap Kompleks Qeydiyyatı");
        helper.setTo("mahammad.yusifov.megaltech@gmail.com");
        helper.addCc("alakbar.abdullayev.megaltech@gmail.com");

        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        String subject = "Kompleks kimi qoşulmaq istəyən var!";

        // Replace placeholders in the HTML template with the actual values
        otpPage = otpPage.replace("$name", registerRequest.getName());
        otpPage = otpPage.replace("$surname", registerRequest.getSurname());
        otpPage = otpPage.replace("$phoneNumber", registerRequest.getPhoneNumber());
        otpPage = otpPage.replace("$email", registerRequest.getEmail());
        otpPage = otpPage.replace("$venueName", "Venue 1");
        otpPage = otpPage.replace("$socialLink", "Social link 1");
        otpPage = otpPage.replace("$datetime", dateTime);

        helper.setSubject(subject);
        helper.setText(otpPage, true);

        mailSender.send(message);
    }

    public void clearOTP(User appUser) {
        appUser.setOneTimePassword(null);
        appUser.setOtpRequestedTime(null);
        userRepository.save(appUser);
    }

    public void checkOtp(String email, String otp) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "Bu email ilə istifadəçi tapılmadı"));

        Timestamp otpExpireTime = user.getOtpRequestedTime();
        otpExpireTime.setTime(otpExpireTime.getTime() + (5 * 60 * 1000));

        if (otpExpireTime.before(new Timestamp(System.currentTimeMillis()))) {
            throw new RuntimeException("OTP time expired");
        }

        if (!otpUtil.isOtpCorrect(otp, user.getOneTimePassword())) {
            throw new WrongOtpException(ErrorCode.WRONG_OTP, "Otp kodu yanlışdır");
        }
        // set new password for user
        clearOTP(user);
    }
}

package com.rashad.bestpractice.service;

import com.rashad.bestpractice.entity.Role;
import com.rashad.bestpractice.entity.User;
import com.rashad.bestpractice.exception.ErrorCode;
import com.rashad.bestpractice.exception.UserAlreadyExistsException;
import com.rashad.bestpractice.exception.UserBlockedException;
import com.rashad.bestpractice.exception.WrongEmailAddressException;
import com.rashad.bestpractice.exception.WrongPasswordFormatException;
import com.rashad.bestpractice.model.request.LoginRequest;
import com.rashad.bestpractice.model.request.RegisterRequest;
import com.rashad.bestpractice.model.response.JwtResponse;
import com.rashad.bestpractice.repository.UserRepository;
import com.rashad.bestpractice.util.Validator;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final EmailService emailService;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterRequest request) {
        if (!Validator.checkEmail(request.getEmail())) {
            throw new WrongEmailAddressException(ErrorCode.WRONG_EMAIL_ADDRESS, "Email adres duzgun formatda deyil");
        }
        if (!Validator.checkPassword(request.getPassword())) {
            throw new WrongPasswordFormatException(ErrorCode.WRONG_PASSWORD_FORMAT, "Password duzgun formatda deyil");
        }
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(ErrorCode.USER_ALREADY_EXISTS, "Sistemdə artıq bu email ilə istifadəçi var");
        }
        if (repository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new UserAlreadyExistsException(ErrorCode.USER_ALREADY_EXISTS, "Sistemdə artıq bu nömrə ilə istifadəçi var");
        }

        User user = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .birthDate(Date.valueOf(request.getBirthDate()))
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .role(Role.USER)
                .build();

        repository.save(user);
        // send email verification for activate user
        try {
            emailService.sendOTPEmail(user, "321898");
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
        return "User created";
    }


    public JwtResponse loginUser(LoginRequest request) {
        String login = request.getLogin();

        User user;
        if (Validator.checkEmail(login)) {
            user = repository.findByEmail(login).orElseThrow(() -> new BadCredentialsException(null));
        } else if (Validator.checkPhoneNumber(login)) {
            user = repository.findByPhoneNumber(login).orElseThrow(() -> new BadCredentialsException(null));
            login = user.getEmail();
        } else {
            throw new BadCredentialsException(null);
        }

        if (user.isAccountBlocked() && user.getBlockTime().after(new Timestamp(System.currentTimeMillis()))) {
            throw new UserBlockedException(ErrorCode.USER_BLOCK, "Zəhmət olmasa, biraz sonra yenidən yoxlayın.");
        } else if (user.getFailedAttempt() >= 3) {
            repository.resetBlockAccountDetails(user.getId());
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            login,
                            request.getPassword()
                    )
            );
            repository.resetBlockAccountDetails(user.getId());
        } catch (AuthenticationException ex) {
            repository.updateFailedAttempts(user.getId());
            throw ex;
        }
        return JwtResponse.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .user(user)
                .build();
    }
}

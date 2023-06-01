package com.rashad.bestpractice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = new HashMap<>();

        if (authException.getMessage().equals("Full authentication is required to access this resource"))
            body.put("message", "Bu api-dən istifadə etmək üçün siz öncə login olmalısız. " +
                    "Bunun üçün zəhmət olmasa yuxarda qeyd olunan " +
                    "Login and Register bölməsindən istifadə edin.");

        if (authException.getMessage().equals("Bad credentials"))
            body.put("message", "Daxil etdiyiniz password düzgün deyil");

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }

//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response,
//                         AuthenticationException authException) throws IOException {
//        Throwable cause = authException.getCause();
//        if (cause instanceof ExpiredJwtException || cause instanceof SignatureException) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.setContentType("application/json");
//            ObjectMapper mapper = new ObjectMapper();
//            String responseMsg = mapper.writeValueAsString(new ErrorResponse(ErrorCode.INVALID_TOKEN,
//                    "Token is expired or invalid."));
//            response.getWriter().write(responseMsg);
//        }else {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//        }
//
//    }
}

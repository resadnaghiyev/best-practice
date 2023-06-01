package com.rashad.bestpractice.controller;

import com.rashad.bestpractice.model.request.LoginRequest;
import com.rashad.bestpractice.model.request.RegisterRequest;
import com.rashad.bestpractice.model.response.CustomResponse;
import com.rashad.bestpractice.model.response.JwtResponse;
import com.rashad.bestpractice.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "1. Login and Register")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Register",
            description = "For the register you have to send body with example like shown below",
            responses = {@ApiResponse(responseCode = "201", description = "Success Response",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE))}
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String message = authenticationService.register(request);
        return new ResponseEntity<>(new CustomResponse(message), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Login",
            description = "For the login you have to send body with example like shown below",
            responses = {@ApiResponse(responseCode = "200", description = "Success Response",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE))}
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        JwtResponse tokens = authenticationService.loginUser(request);
        return new ResponseEntity<>(new CustomResponse(tokens), HttpStatus.OK);
    }


}

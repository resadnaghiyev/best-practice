package com.rashad.bestpractice.controller;

import com.rashad.bestpractice.entity.User;
import com.rashad.bestpractice.model.response.CustomResponse;
import com.rashad.bestpractice.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@SecurityRequirement(name = "BearerJwt")
@Tag(name = "2. User Controller")
public class UserController {

    private final UserRepository userRepository;

    @Operation(
            summary = "This is summary",
            description = "This is description",
            responses = {@ApiResponse(responseCode = "200", description = "Success Response",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE))}
    )
    @GetMapping("/get-all")
    public ResponseEntity<?> getAllUsers() {
        List<User> data = userRepository.findAll();
        if (data.isEmpty()) {
            String message = "bazada user tapilmadi";
            return new ResponseEntity<>(new CustomResponse(false, message), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new CustomResponse(data), HttpStatus.OK);
    }

    @Operation(
            summary = "This is summary",
            description = "This is description",
            parameters = {@Parameter(name = "userId", description = "id", example = "1")},
            responses = {@ApiResponse(responseCode = "200", description = "Success Response",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE))}
    )
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        Optional<User> data = userRepository.findById(userId);
        if (data.isEmpty()) {
            String message = "user tapilmadi";
            return new ResponseEntity<>(new CustomResponse(false, message), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new CustomResponse(data.get()), HttpStatus.OK);
    }

}

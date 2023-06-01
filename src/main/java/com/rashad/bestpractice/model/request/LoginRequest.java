package com.rashad.bestpractice.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "required should not be empty")
    @Schema(example = "resad")
    private String login;

    @NotBlank(message = "required should not be empty")
    @Schema(example = "123@Resad")
    private String password;
}


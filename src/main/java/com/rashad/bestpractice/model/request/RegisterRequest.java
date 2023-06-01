package com.rashad.bestpractice.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rashad.bestpractice.util.SqlDateDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Size(max = 20)
    @NotBlank(message = "required should not be null or empty")
    @Schema(example = "Rashad")
    private String name;

    @Size(max = 20)
    @NotBlank(message = "required should not be null or empty")
    @Schema(example = "Naghiyev")
    private String surname;

    @NotBlank(message = "required should not be null or empty")
    @Schema(example = "31.12.2023")
    @JsonDeserialize(using = SqlDateDeserializer.class)
    private LocalDate birthDate;

    @NotBlank(message = "required should not be null or empty")
    @Schema(example = "+994504453278")
    private String phoneNumber;

    @NotBlank(message = "required should not be null or empty")
    @Schema(example = "example@gmail.com")
    private String email;

    @NotBlank(message = "required should not be null or empty")
    @Schema(example = "123@Resad",
            description = "Password must be at least 8 characters long and " +
                    "should contain at least one upper, " +
                    "one lower and one special character(@#$%^&+=).")
    private String password;
}

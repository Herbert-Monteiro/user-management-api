package com.herbertmonteiro.usermanagementapi.dto;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserRequestDTO (
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "CPF is required")
        String cpf,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,

        String role,

        @NotNull(message = "Birth date is required")
        @Past(message = "Birth date must be in the past")
        LocalDate birthDate

){}


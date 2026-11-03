package com.herbertmonteiro.usermanagementapi.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegisterRequestDTO (
        @NotBlank(message = "Name is required")
        String nome,

        @NotBlank(message = "CPF is required")
        String cpf,

        @NotBlank(message = "Email is required")
        @Email(regexp = ValidationPatterns.EMAIL, message = "Invalid email")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,

        @NotNull(message = "Birth date is required")
        @Past(message = "Birth date must be in the past")
        LocalDate birthDate
){
        public final class ValidationPatterns {
                private ValidationPatterns() {}
                public static final String EMAIL =
                        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$";
        }
}

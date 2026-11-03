package com.herbertmonteiro.usermanagementapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UserUpdateDTO(
        @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
        String name,

        String cpf,

        @Email(regexp = ValidationPatterns.EMAIL, message = "Invalid email")
        String email,

        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,

        @Past(message = "Birth date must be in the past")
        LocalDate birthDate
) {
        public final class ValidationPatterns {
        private ValidationPatterns() {}
        public static final String EMAIL =
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$";
}}

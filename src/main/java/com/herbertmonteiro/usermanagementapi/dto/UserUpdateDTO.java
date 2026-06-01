package com.herbertmonteiro.usermanagementapi.dto;

import jakarta.validation.constraints.Email;
import java.time.LocalDate;

public record UserUpdateDTO(
        String name,
        String cpf,

        @Email(message = "Invalid email")
        String email,

        String password,
        LocalDate birthDate
) {}

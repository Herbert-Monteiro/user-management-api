package com.herbertmonteiro.usermanagementapi.dto;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String name,
        String cpf,
        String email,
        String role,
        LocalDate birthDate,
        LocalDateTime createdAt
    ){}




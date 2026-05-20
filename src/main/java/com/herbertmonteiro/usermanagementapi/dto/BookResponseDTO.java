package com.herbertmonteiro.usermanagementapi.dto;

import java.time.LocalDateTime;

public record BookResponseDTO(
        Long id,
        String title,
        String author,
        String isbn,
        Integer quantity,
        LocalDateTime createdAt
) {}

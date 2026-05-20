package com.herbertmonteiro.usermanagementapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanResponseDTO(
        Long id,
        Long userId,
        String userName,
        Long bookId,
        String bookTitle,
        LocalDate loanDate,
        LocalDate returnDate,
        Integer loanDays,
        BigDecimal dailyRate,
        BigDecimal totalPrice,
        BigDecimal totalWithFine

) {}

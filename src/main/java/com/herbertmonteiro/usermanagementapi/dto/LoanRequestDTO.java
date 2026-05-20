package com.herbertmonteiro.usermanagementapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record LoanRequestDTO(
        @NotNull(message = "Book is required")
        Long bookid,

        @NotNull(message = "Loan days is required")
        @Min(value = 1, message = "Loan days must be at least 1")
        Integer loanDays,

        @NotNull(message = "Daily rate is required")
        @DecimalMin(value = "0.01", message = "Daily rate must be greater than 0")
        BigDecimal dailyRate
) {}

package com.herbertmonteiro.usermanagementapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public record LoanRequestDTO(
        @NotNull(message = "Book is required")
        Long bookid,

        @NotNull(message = "Loan days is required")
        @Min(value = 1, message = "Loan days must be at least 1")
        Integer loanDays
) {}

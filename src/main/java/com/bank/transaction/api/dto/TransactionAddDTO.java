package com.bank.transaction.api.dto;

import com.bank.transaction.infrastructure.entity.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionAddDTO {
    @NotBlank(message = "ID cannot be blank")
    private String id;

    @NotBlank(message = "Account number cannot be blank")
    private String accountNumber;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Type cannot be null")
    private TransactionType type;

    private String description;
}
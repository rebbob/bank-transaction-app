package com.bank.transaction.api.dto;

import com.bank.transaction.infrastructure.entity.TransactionType;
import lombok.Data;

@Data
public class TransactionQueryDTO {
    private String transactionId;

    private String accountNumber;

    private TransactionType type;
}
package com.bank.transaction.infrastructure.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Transaction {
    private String id;
    private String accountNumber;
    private BigDecimal amount;
    private TransactionType type;
    private Date timestamp;
    private String description;

}
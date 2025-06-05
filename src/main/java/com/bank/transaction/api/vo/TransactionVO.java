package com.bank.transaction.api.vo;

import com.bank.transaction.infrastructure.entity.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionVO {
    private String id;
    private String accountNumber;
    private BigDecimal amount;
    private TransactionType type;
    private Date timestamp;
    private String description;
}
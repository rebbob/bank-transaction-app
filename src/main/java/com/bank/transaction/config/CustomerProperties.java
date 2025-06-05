package com.bank.transaction.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class CustomerProperties {
    @Value("${bank.transaction.maxCount:1000}")
    private int maxCount;

    @Value("${bank.cache.capacity:100}")
    private int capacity;
}

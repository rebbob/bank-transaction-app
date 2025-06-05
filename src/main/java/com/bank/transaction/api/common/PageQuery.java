package com.bank.transaction.api.common;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PageQuery<T> {
    @NotNull(message = "Page number cannot be null")
    @Min(1)
    private Integer pageNumber;

    @NotNull(message = "Page size cannot be null")
    @Min(10)
    private Integer pageSize;

    private T param;

}

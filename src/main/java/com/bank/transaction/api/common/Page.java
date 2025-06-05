package com.bank.transaction.api.common;

import lombok.Data;

import java.util.List;
@Data
public class Page<T> {
    private int pageNumber;
    private int pageSize;
    private int totalCount;
    private List<T> list;
}

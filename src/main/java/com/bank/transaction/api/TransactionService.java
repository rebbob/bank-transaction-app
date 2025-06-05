package com.bank.transaction.api;

import com.bank.transaction.api.common.Page;
import com.bank.transaction.api.common.PageQuery;
import com.bank.transaction.api.dto.TransactionAddDTO;
import com.bank.transaction.api.dto.TransactionModifyDTO;
import com.bank.transaction.api.dto.TransactionQueryDTO;
import com.bank.transaction.api.vo.TransactionVO;

public interface TransactionService {
    String createTransaction(TransactionAddDTO transaction);

    void updateTransaction(TransactionModifyDTO transaction);

    void deleteTransaction(String transactionId);

    Page<TransactionVO> page(PageQuery<TransactionQueryDTO> param);

}
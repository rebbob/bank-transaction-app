package com.bank.transaction.application;

import com.bank.transaction.api.common.Page;
import com.bank.transaction.api.common.PageQuery;
import com.bank.transaction.api.TransactionService;
import com.bank.transaction.api.dto.TransactionAddDTO;
import com.bank.transaction.api.dto.TransactionModifyDTO;
import com.bank.transaction.api.dto.TransactionQueryDTO;
import com.bank.transaction.api.vo.TransactionVO;
import com.bank.transaction.domain.TransactionManager;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionManager transactionManager;

    public TransactionServiceImpl(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public String createTransaction(TransactionAddDTO dto) {
        return transactionManager.createTransaction(dto);
    }

    @Override
    public Page<TransactionVO> page(PageQuery<TransactionQueryDTO> query) {
        return transactionManager.page(query);
    }

    @Override
    public void updateTransaction(TransactionModifyDTO dto) {
        transactionManager.updateTransaction(dto);
    }

    @Override
    public void deleteTransaction(String id) {
        transactionManager.deleteTransaction(id);
    }

}
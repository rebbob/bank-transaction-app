package com.bank.transaction.infrastructure;

import com.bank.transaction.config.CustomerProperties;
import com.bank.transaction.infrastructure.entity.Transaction;
import com.bank.transaction.common.exception.BizException;
import com.bank.transaction.api.common.Page;
import com.bank.transaction.api.common.PageQuery;
import com.bank.transaction.api.dto.TransactionQueryDTO;
import com.bank.transaction.api.vo.TransactionVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.bank.transaction.api.common.CodeEnum.ERROR_TOO_MANY_TRANS;

@Repository
public class TransactionRepository {
    private final Map<String, Transaction> transactions = new ConcurrentHashMap<>();

    @Resource
    private CustomerProperties customerProperties;

    public Transaction save(Transaction transaction) {
        //avoid oom
        if (customerProperties.getMaxCount() <= transactions.size()) {
            throw new BizException(ERROR_TOO_MANY_TRANS);
        }
        transactions.put(transaction.getId(), transaction);
        return transaction;
    }

    public Transaction findById(String id) {
        return transactions.get(id);
    }

    public List<Transaction> findAll() {
        return new ArrayList<>(transactions.values());
    }

    public List<Transaction> findByAccountNumber(String accountNumber) {
        return transactions.values().stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .collect(Collectors.toList());
    }

    public void deleteById(String id) {
        transactions.remove(id);
    }

    public Page<TransactionVO> findByPage(PageQuery<TransactionQueryDTO> param) {
        Integer pageNum = param.getPageNumber();
        Integer pageSize = param.getPageSize();

        TransactionQueryDTO dto = param.getParam();

        List<Transaction> dataList = transactions.values().stream().filter(e -> {
            if (dto == null) {
                return true;
            }
            if (dto.getAccountNumber() != null && !e.getAccountNumber().equals(dto.getAccountNumber())) {
                return false;
            }
            if (dto.getTransactionId() != null && !e.getId().equals(dto.getTransactionId())) {
                return false;
            }
            if (dto.getType() != null && e.getType() != dto.getType()) {
                return false;
            }
            return true;
        }).sorted((o1, o2) ->o2.getTimestamp().compareTo(o1.getTimestamp())).toList();

        //分页
        List<TransactionVO> list = dataList.stream().skip((pageNum - 1) * pageSize).limit(pageSize).map(e -> {
            TransactionVO vo = new TransactionVO();
            BeanUtils.copyProperties(e, vo);
            return vo;
        }).toList();

        Page<TransactionVO> page = new Page<>();
        page.setPageNumber(pageNum);
        page.setPageSize(pageSize);
        page.setList(list);
        page.setTotalCount(list.size());
        return page;
    }
}
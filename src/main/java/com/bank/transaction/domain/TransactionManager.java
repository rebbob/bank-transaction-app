package com.bank.transaction.domain;

import com.bank.transaction.api.common.Page;
import com.bank.transaction.api.common.PageQuery;
import com.bank.transaction.api.dto.TransactionAddDTO;
import com.bank.transaction.api.dto.TransactionModifyDTO;
import com.bank.transaction.api.dto.TransactionQueryDTO;
import com.bank.transaction.api.vo.TransactionVO;
import com.bank.transaction.common.exception.BizException;
import com.bank.transaction.common.util.LockUtils;
import com.bank.transaction.common.util.TransPageCacheUtils;
import com.bank.transaction.common.constant.CacheKeyConstant;
import com.bank.transaction.infrastructure.TransactionRepository;
import com.bank.transaction.infrastructure.entity.Transaction;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.bank.transaction.api.common.CodeEnum.*;

@Component
public class TransactionManager {

    private final TransactionRepository transactionRepository;

    public TransactionManager(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public String createTransaction(TransactionAddDTO dto) {
        String lockKey = CacheKeyConstant.createLockKey(dto.getId());
        boolean lock = LockUtils.lock(lockKey, 10, TimeUnit.SECONDS);
        if (!lock) {
            throw new BizException(ERROR_REPEAT_CREATE_TRANS);
        }
        Transaction db = transactionRepository.findById(dto.getId());
        if (db != null) {
            throw new BizException(ERROR_REPEAT_CREATE_TRANS);
        }
        try {
            Transaction trans = new Transaction();
            BeanUtils.copyProperties(dto, trans);
            trans.setTimestamp(new Date());
            transactionRepository.save(trans);
            //清除用户分页缓存
            TransPageCacheUtils.remove(trans.getAccountNumber());

            return trans.getId();
        } finally {
            LockUtils.unLock(lockKey);
        }
    }

    public Page<TransactionVO> page(PageQuery<TransactionQueryDTO> query) {
        TransactionQueryDTO dto = query.getParam();
        String key = null;
        if (dto != null && dto.getAccountNumber() != null) {
            key = query.getPageNumber() + "_" + query.getPageSize()
                    + "_" + dto.getAccountNumber() + "_" + dto.getTransactionId() + "_" + dto.getType();
            Page<TransactionVO> transFromCache = TransPageCacheUtils.getPageTransFromCache(dto.getAccountNumber(), key);
            if (transFromCache != null) {
                return transFromCache;
            }
        }

        Page<TransactionVO> page = transactionRepository.findByPage(query);
        //每个账户的分页数据 加入缓存
        if (key != null) {
            TransPageCacheUtils.putPageTransToCache(dto.getAccountNumber(), key, page);
        }
        return page;
    }

    public void updateTransaction(TransactionModifyDTO dto) {
        String lockKey = CacheKeyConstant.updateLockKey(dto.getId());
        boolean lock = LockUtils.lock(lockKey, 10, TimeUnit.SECONDS);
        if (!lock) {
            throw new BizException(ERROR_REPEAT_MODIFY_TRANS);
        }

        Transaction transaction = transactionRepository.findById(dto.getId());
        if (transaction == null) {
            throw new BizException(ERROR_TRANS_NOT_FOUND);
        }
        try {
            if (dto.getAmount() != null) {
                transaction.setAmount(dto.getAmount());
            }
            if (dto.getDescription() != null) {
                transaction.setDescription(dto.getDescription());
            }

            BeanUtils.copyProperties(dto, transaction);
            transactionRepository.save(transaction);

            TransPageCacheUtils.remove(transaction.getAccountNumber());
        } finally {
            LockUtils.unLock(lockKey);
        }
    }

    public void deleteTransaction(String id) {
        String lockKey = CacheKeyConstant.delLockKey(id);
        boolean lock = LockUtils.lock(lockKey, 10, TimeUnit.SECONDS);
        if (!lock) {
            throw new BizException(ERROR_REPEAT_DEL_TRANS);
        }
        Transaction transaction = transactionRepository.findById(id);
        if (transaction == null) {
            throw new BizException(ERROR_TRANS_NOT_FOUND);
        }

        try {
            transactionRepository.deleteById(id);
            TransPageCacheUtils.remove(transaction.getAccountNumber());
        } finally {
            LockUtils.unLock(lockKey);
        }
    }

}
package com.bank.transaction.common.util;


import com.bank.transaction.common.LRUCache;
import com.bank.transaction.config.CustomerProperties;
import com.bank.transaction.api.common.Page;
import com.bank.transaction.api.vo.TransactionVO;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;


/**
 * 用户交易分页数据缓存
 */
@Component
public class TransPageCacheUtils {

    //缓存用户分页结果
    static LRUCache<String, Pair<String,Page<TransactionVO>>> accountPageTransCache = null;

    public TransPageCacheUtils(CustomerProperties properties) {
        TransPageCacheUtils.accountPageTransCache = new LRUCache<>(properties.getCapacity());
    }
    public static Page<TransactionVO> getPageTransFromCache(String accountNumber,String key) {
        Pair<String, Page<TransactionVO>> pair = accountPageTransCache.get(accountNumber);
        if (pair == null) {
            return null;
        }
        if (pair.getLeft().equals(key)) {
            return pair.getRight();
        }
        //key不一致，删除缓存
        TransPageCacheUtils.accountPageTransCache.remove(accountNumber);
        return null;
    }

    public static void putPageTransToCache(String accountNumber, String key, Page<TransactionVO> page) {
        TransPageCacheUtils.accountPageTransCache.put(accountNumber, Pair.of(key, page));
    }

    /**
     * 清除用户分页缓存
     * @param accountNumber
     */
    public static void remove(String accountNumber) {
        TransPageCacheUtils.accountPageTransCache.remove(accountNumber);
    }
}


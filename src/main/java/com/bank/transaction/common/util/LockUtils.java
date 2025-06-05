package com.bank.transaction.common.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class LockUtils {
    private static final Map<String, Long> lockMap = new ConcurrentHashMap<>();

    public static boolean lock(String key, long ttl, TimeUnit unit) {
        long now = System.currentTimeMillis();
        long expireTime = now + unit.toMillis(ttl);

        Long lastExpTime = lockMap.putIfAbsent(key, expireTime);
        if (lastExpTime == null) {
            return true;
        }
        if (now >= lastExpTime) {
            synchronized (LockUtils.class) {
                lastExpTime = lockMap.get(key);
                if (lastExpTime == null || now >= lastExpTime) {
                    lockMap.put(key, expireTime);
                    return true;
                }
            }
        }
        return false;
    }
    public static void unLock(String key) {
        lockMap.remove(key);
    }

}
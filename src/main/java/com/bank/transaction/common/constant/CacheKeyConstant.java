package com.bank.transaction.common.constant;

public class CacheKeyConstant {
    public static final String CREATE_TRANS_KEY = "create_trans_req_key:";
    public static final String DELETE_TRANS_KEY = "delete_trans_key:";
    public static final String UPDATE_TRANS_KEY = "update_trans_key:";

    public static String createLockKey(String requestId) {
        return CREATE_TRANS_KEY + requestId;
    }

    public static String delLockKey(String id) {
        return DELETE_TRANS_KEY + id;
    }

    public static String updateLockKey(String id) {
        return UPDATE_TRANS_KEY + id;
    }
}

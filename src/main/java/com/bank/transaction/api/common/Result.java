package com.bank.transaction.api.common;

import lombok.Data;

@Data
public class Result<T> {
    private String code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        return getInstance(CodeEnum.SUCCESS,data);
    }
    public static <T> Result<T> fail(CodeEnum code) {
        return getInstance(code,null);
    }
    public static <T> Result<T> fail(String code,String msg) {
        Result<T> result = new Result<T>();
        result.code = code;
        result.message = msg;
        return result;
    }
    public static <T> Result<T> getInstance(CodeEnum codeEnum,T data) {
        Result<T> result = new Result<T>();
        result.data = data;
        result.code = codeEnum.getCode();
        result.message = codeEnum.getMessage();
        return result;
    }

}

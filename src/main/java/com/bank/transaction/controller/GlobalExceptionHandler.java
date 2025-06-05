package com.bank.transaction.controller;

import com.bank.transaction.common.exception.BizException;
import com.bank.transaction.api.common.CodeEnum;
import com.bank.transaction.api.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BizException.class)
    public Result<String> bizExp(BizException e) {
        logger.error("bizExp:", e);
        CodeEnum er = e.getExceptionEnum();
        return Result.fail(er);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> vaildExp(MethodArgumentNotValidException e) {
        logger.error("vaildExp:", e);

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        List<String> errors = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return Result.fail(CodeEnum.ERROR_BAD_REQUEST.getCode(), String.join(",", errors));
    }

    @ExceptionHandler(Exception.class)
    public Result<String> unkownExp(Exception e) {
        logger.error("unkownExp:", e);
        return Result.fail(CodeEnum.ERROR_INTERNAL_SERVER);
    }
}

package com.bank.transaction.api.common;

import lombok.Getter;

public enum CodeEnum {
    SUCCESS("200","Success"),


    ERROR_REPEAT_CREATE_TRANS("E0001", "repeat create transaction"),
    ERROR_TRANS_NOT_FOUND("E0002", "transaction not found"),
    ERROR_REPEAT_MODIFY_TRANS("E0003", "repeat modify transaction"),
    ERROR_REPEAT_DEL_TRANS("E0004", "repeat delete transaction"),
    ERROR_TOO_MANY_TRANS("E0005", "Too many transactions"),
    ERROR_BAD_REQUEST("E0006","bad request"),

    ERROR_INTERNAL_SERVER("E0099","internal server error"),

    ;
    @Getter
    private String code;
    @Getter
    private String message;

    CodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

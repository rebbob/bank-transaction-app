package com.bank.transaction.common.exception;

import com.bank.transaction.api.common.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * custom exception
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BizException extends RuntimeException{
   private CodeEnum exceptionEnum;
}

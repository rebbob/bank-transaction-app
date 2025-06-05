package com.bank.transaction.controller;

import com.bank.transaction.api.common.Page;
import com.bank.transaction.api.common.PageQuery;
import com.bank.transaction.api.common.Result;
import com.bank.transaction.api.dto.TransactionAddDTO;
import com.bank.transaction.api.dto.TransactionModifyDTO;
import com.bank.transaction.api.dto.TransactionQueryDTO;
import com.bank.transaction.api.vo.TransactionVO;
import com.bank.transaction.api.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @Operation(summary = "新增交易", description = "新增交易")
    @PostMapping("/createTransaction")
    public Result<String> createTransaction(@RequestBody @Valid TransactionAddDTO addDTO) {
        return Result.success(transactionService.createTransaction(addDTO));
    }

    @Operation(summary = "分页查询", description = "分页查询")
    @PostMapping("/page")
    public Result<Page<TransactionVO>> page(@RequestBody @Valid PageQuery<TransactionQueryDTO> param) {
        Page<TransactionVO> page = transactionService.page(param);
        return Result.success(page);
    }

    @Operation(summary = "更新交易", description = "更新交易")
    @PostMapping("/updateTransaction")
    public Result<String> updateTransaction(@RequestBody @Valid TransactionModifyDTO modifyDTO) {
        transactionService.updateTransaction(modifyDTO);
        return Result.success(modifyDTO.getId());
    }
    @Operation(summary = "删除一条", description = "删除一条")
    @PostMapping("/deleteTransaction/{id}")
    public Result<Void> deleteTransaction(@PathVariable String id) {
        transactionService.deleteTransaction(id);
        return Result.success(null);
    }
}
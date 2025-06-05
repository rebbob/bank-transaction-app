package com.bank.transaction.controller;

import com.bank.transaction.BankTransactionApplication;
import com.bank.transaction.infrastructure.entity.TransactionType;
import com.bank.transaction.api.common.CodeEnum;
import com.bank.transaction.api.common.Page;
import com.bank.transaction.api.common.PageQuery;
import com.bank.transaction.api.common.Result;
import com.bank.transaction.api.dto.TransactionAddDTO;
import com.bank.transaction.api.dto.TransactionModifyDTO;
import com.bank.transaction.api.dto.TransactionQueryDTO;
import com.bank.transaction.api.vo.TransactionVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(classes = BankTransactionApplication.class)
@AutoConfigureMockMvc
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createTransaction() throws Exception {
        TransactionAddDTO transaction = new TransactionAddDTO();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setAccountNumber("123456");
        transaction.setAmount(BigDecimal.valueOf(1000.0));
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setDescription("Salary deposit");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(transaction);

        MvcResult mvcResult = mockMvc.perform(post("/api/transactions/createTransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        Result<String> result = objectMapper.readValue(contentAsString, new TypeReference<Result<String>>() {});
        assertEquals(result.getCode(), CodeEnum.SUCCESS.getCode());
    }

    /**
     * 非法金额测试
     * @throws Exception
     */
    @Test
    public void createTransactionIllegalAmount() throws Exception {
        TransactionAddDTO transaction = new TransactionAddDTO();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setAccountNumber("123456");
        transaction.setAmount(BigDecimal.valueOf(0));
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setDescription("Salary deposit");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(transaction);

        MvcResult mvcResult = mockMvc.perform(post("/api/transactions/createTransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        Result<String> result = objectMapper.readValue(contentAsString, new TypeReference<Result<String>>() {});
        assertEquals(result.getCode(), CodeEnum.ERROR_BAD_REQUEST.getCode());
    }

    @Test
    public void updateTransaction() throws Exception {
        //先创建一条数据
        TransactionAddDTO transaction = new TransactionAddDTO();
        transaction.setId(UUID.randomUUID().toString());

        transaction.setAccountNumber("123456");
        transaction.setAmount(BigDecimal.valueOf(1000.0));
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setDescription("Salary deposit");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(transaction);

        MvcResult mvcResult = mockMvc.perform(post("/api/transactions/createTransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        String resp = mvcResult.getResponse().getContentAsString();
        Result<String> result = objectMapper.readValue(resp, new TypeReference<Result<String>>() {});

        assertEquals(result.getCode(), CodeEnum.SUCCESS.getCode());
        //修改数据
        TransactionModifyDTO modifyDTO = new TransactionModifyDTO();
        modifyDTO.setAccountNumber("123456");
        modifyDTO.setAmount(BigDecimal.valueOf(100));
        modifyDTO.setDescription("Salary deposit");
        modifyDTO.setId(transaction.getId());

        String json2 = objectMapper.writeValueAsString(modifyDTO);

        MvcResult mvcResult1 = mockMvc.perform(post("/api/transactions/updateTransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andReturn();
        String resp2 = mvcResult1.getResponse().getContentAsString();
        Result<String> result2 = objectMapper.readValue(resp2, new TypeReference<Result<String>>() {});

        assertEquals(result2.getCode(), CodeEnum.SUCCESS.getCode());

    }

    @Test
    public void deleteTransaction() throws Exception {
        //先创建一条数据
        TransactionAddDTO transaction = new TransactionAddDTO();
        transaction.setId(UUID.randomUUID().toString());

        transaction.setAccountNumber("123456");
        transaction.setAmount(BigDecimal.valueOf(1000.0));
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setDescription("Salary deposit");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(transaction);

        MvcResult mvcResult = mockMvc.perform(post("/api/transactions/createTransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        //删除数据
        MvcResult mvcResult1 = mockMvc.perform(post("/api/transactions/deleteTransaction/" + transaction.getId()))
                .andReturn();

        Result<String> result2 = objectMapper.readValue(mvcResult1.getResponse().getContentAsString(), new TypeReference<Result<String>>() {});
        assertEquals(result2.getCode(), CodeEnum.SUCCESS.getCode());

    }

    @Test
    public void page() throws Exception {
        //先创建一条数据
        createTransaction();

        PageQuery<TransactionQueryDTO> query = new PageQuery<>();
        query.setPageSize(10);
        query.setPageNumber(1);
        TransactionQueryDTO transactionQueryDTO = new TransactionQueryDTO();
        transactionQueryDTO.setAccountNumber("123456");
        transactionQueryDTO.setType(TransactionType.DEPOSIT);
        query.setParam(transactionQueryDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(query);

        MvcResult mvcResult = mockMvc.perform(post("/api/transactions/page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        Result<Page<TransactionVO>> result2 = objectMapper.readValue(contentAsString, new TypeReference<Result<Page<TransactionVO>>>() {});
        assertEquals(result2.getCode(), CodeEnum.SUCCESS.getCode());
    }

}
package com.frauddetection.controller;

import com.frauddetection.model.Transaction;
import com.frauddetection.service.FraudDetectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//本地测试时候调用的API： 
//http://localhost:8080/api/transactions/process

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final FraudDetectionService fraudDetectionService;

    @PostMapping("/process")
    public ResponseEntity<Transaction> processTransaction(@RequestBody Transaction transaction) {
        fraudDetectionService.processTransaction(transaction);
        return ResponseEntity.ok(transaction);
    }
} 
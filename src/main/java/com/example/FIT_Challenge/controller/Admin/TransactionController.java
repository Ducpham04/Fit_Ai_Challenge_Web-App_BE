package com.example.FIT_Challenge.controller.Admin;

import com.example.FIT_Challenge.DTO.TransactionDTO.TransactionRequest;
import com.example.FIT_Challenge.DTO.TransactionDTO.TransactionResponse;
import com.example.FIT_Challenge.config.NotificationResponse;
import com.example.FIT_Challenge.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    @Autowired
    private final TransactionService transactionService;

    /**
     * ➕ Tạo mới giao dịch
     */
    @PostMapping
    public ResponseEntity<NotificationResponse> create(@RequestBody TransactionRequest request) {
        NotificationResponse response = transactionService.createTransaction(request);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    /**
     * 📋 Lấy danh sách tất cả giao dịch
     */
    @GetMapping
    public ResponseEntity<NotificationResponse> getAllTransactions() {
        NotificationResponse response = transactionService.getAllTransactions();

        return ResponseEntity.ok(response);
    }

    /**
     * 🔍 Lấy thông tin giao dịch theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getTransactionById(@PathVariable Long id) {
        TransactionResponse data = transactionService.getTransactionById(id);
        if (data != null) {
            return ResponseEntity.ok(new NotificationResponse(true, "Transaction found", data));
        } else {
            return ResponseEntity.status(404).body(new NotificationResponse(false, "Transaction not found"));
        }
    }

    /**
     * ✏️ Cập nhật thông tin giao dịch
     */
    @PutMapping("/{id}")
    public ResponseEntity<NotificationResponse> updateTransaction(
            @PathVariable Long id,
            @RequestBody TransactionRequest request) {
        NotificationResponse response = transactionService.updateTransaction(id, request);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    /**
     * ❌ Xóa giao dịch theo ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<NotificationResponse> deleteTransaction(@PathVariable Long id) {
        NotificationResponse response = transactionService.deleteTransaction(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 404).body(response);
    }
}

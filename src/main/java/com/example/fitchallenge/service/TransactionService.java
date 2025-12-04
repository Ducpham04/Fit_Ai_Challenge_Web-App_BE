package com.example.fitchallenge.service;

import com.example.fitchallenge.DTO.TransactionDTO.TransactionRequest;
import com.example.fitchallenge.DTO.TransactionDTO.TransactionResponse;
import com.example.fitchallenge.config.NotificationResponse;

public interface TransactionService {

    /**
     * ➕ Tạo mới giao dịch
     */
    NotificationResponse createTransaction(TransactionRequest request);

    /**
     * 📋 Lấy danh sách tất cả giao dịch
     */
    NotificationResponse getAllTransactions();

    /**
     * 🔍 Lấy giao dịch theo ID
     */
    TransactionResponse getTransactionById(Long id);

    /**
     * ✏️ Cập nhật giao dịch
     */
    NotificationResponse updateTransaction(Long id, TransactionRequest request);

    /**
     * ❌ Xóa giao dịch
     */
    NotificationResponse deleteTransaction(Long id);

    /**
     * 👤 Lấy danh sách giao dịch theo user ID
     */
    NotificationResponse getTransactionsByUser(Long userId);
}

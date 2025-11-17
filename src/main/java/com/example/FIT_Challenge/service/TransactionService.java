package com.example.FIT_Challenge.service;

import com.example.FIT_Challenge.DTO.TransactionDTO.TransactionRequest;
import com.example.FIT_Challenge.DTO.TransactionDTO.TransactionResponse;
import com.example.FIT_Challenge.config.NotificationResponse;

import java.util.List;

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

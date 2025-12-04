package com.example.fitchallenge.service.impl;

import com.example.fitchallenge.DTO.TransactionDTO.TransactionRequest;
import com.example.fitchallenge.DTO.TransactionDTO.TransactionResponse;
import com.example.fitchallenge.Entity.Transaction;
import com.example.fitchallenge.Entity.User;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.repository.TransactionRepository;
import com.example.fitchallenge.repository.User.UserRepository;
import com.example.fitchallenge.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    // Chuyển entity -> DTO
    private TransactionResponse toResponse(Transaction t) {
        return new TransactionResponse(
                t.getId(),
                t.getUser().getId(),
                t.getUser().getUserName(),
                t.getType(),
                t.getAmount(),
                t.getPoints(),
                t.getReference(),
                t.getStatus(),
                t.getDescription(),
                t.getCreatedAt()
        );
    }

    @Override
    public NotificationResponse createTransaction(TransactionRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transaction tx = new Transaction();
        tx.setUser(user);
        tx.setType(request.getType());
        tx.setAmount(request.getAmount());
        tx.setPoints(request.getPoints());
        tx.setReference(request.getReference());
        tx.setDescription(request.getDescription());
        tx.setStatus(request.getStatus() != null ? request.getStatus() : "completed");

        transactionRepository.save(tx);

        return new NotificationResponse(true, "Transaction created successfully", toResponse(tx));
    }

    @Override
    public NotificationResponse getAllTransactions() {
        List<TransactionResponse> list = transactionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return new NotificationResponse(true, "Success", list);
    }

    @Override
    public TransactionResponse getTransactionById(Long id) {
        return null;
    }

    @Override
    public NotificationResponse getTransactionsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            return new NotificationResponse(false, "User not found");
        }
        List<TransactionResponse> list = transactionRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return new NotificationResponse(true, "Success", list);
    }



    public NotificationResponse updateTransaction(Long id, TransactionRequest request) {
        Transaction tx = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (request.getType() != null) tx.setType(request.getType());
        if (request.getAmount() != null) tx.setAmount(request.getAmount());
        if (request.getPoints() != null) tx.setPoints(request.getPoints());
        if (request.getReference() != null) tx.setReference(request.getReference());
        if (request.getDescription() != null) tx.setDescription(request.getDescription());
        if (request.getStatus() != null) tx.setStatus(request.getStatus());

        transactionRepository.save(tx);
        return new NotificationResponse(true, "Transaction updated successfully", toResponse(tx));
    }

    public NotificationResponse deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            return new NotificationResponse(false, "Transaction not found");
        }
        transactionRepository.deleteById(id);
        return new NotificationResponse(true, "Transaction deleted successfully");
    }
}

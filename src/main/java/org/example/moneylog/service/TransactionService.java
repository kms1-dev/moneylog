package org.example.moneylog.service;

import lombok.RequiredArgsConstructor;
import org.example.moneylog.domain.Category;
import org.example.moneylog.domain.Transaction;
import org.example.moneylog.domain.User;
import org.example.moneylog.dto.TransactionRequest;
import org.example.moneylog.dto.TransactionResponse;
import org.example.moneylog.exception.CategoryNotFoundException;
import org.example.moneylog.exception.TransactionNotFoundException;
import org.example.moneylog.repository.CategoryRepository;
import org.example.moneylog.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public Page<TransactionResponse> getMonthlyTransactions(Long userId, YearMonth yearMonth, Pageable pageable) {
        LocalDate from = yearMonth.atDay(1);
        LocalDate to = yearMonth.atEndOfMonth();
        return transactionRepository
                .findByUserIdAndTransactionDateBetweenOrderByTransactionDateDesc(userId, from, to, pageable)
                .map(TransactionResponse::new);
    }

    public TransactionResponse getTransaction(Long userId, Long transactionId) {
        Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, userId)
                .orElseThrow(TransactionNotFoundException::new);
        return new TransactionResponse(transaction);
    }

    @Transactional
    public TransactionResponse createTransaction(User user, TransactionRequest request) {
        Category category = categoryRepository.findByIdAndUserId(request.getCategoryId(), user.getId())
                .orElseThrow(CategoryNotFoundException::new);
        Transaction transaction = new Transaction(
                user, category, request.getType(), request.getAmount(),
                request.getDescription(), request.getTransactionDate());
        transactionRepository.save(transaction);
        return new TransactionResponse(transaction);
    }

    @Transactional
    public TransactionResponse updateTransaction(Long userId, Long transactionId, TransactionRequest request) {
        Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, userId)
                .orElseThrow(TransactionNotFoundException::new);
        Category category = categoryRepository.findByIdAndUserId(request.getCategoryId(), userId)
                .orElseThrow(CategoryNotFoundException::new);
        transaction.update(category, request.getType(), request.getAmount(),
                request.getDescription(), request.getTransactionDate());
        return new TransactionResponse(transaction);
    }

    @Transactional
    public void deleteTransaction(Long userId, Long transactionId) {
        Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, userId)
                .orElseThrow(TransactionNotFoundException::new);
        transactionRepository.delete(transaction);
    }
}

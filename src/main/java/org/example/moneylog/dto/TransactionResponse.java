package org.example.moneylog.dto;

import lombok.Getter;
import org.example.moneylog.domain.CategoryType;
import org.example.moneylog.domain.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class TransactionResponse {

    private final Long id;
    private final CategoryType type;
    private final Long amount;
    private final Long categoryId;
    private final String categoryName;
    private final String description;
    private final LocalDate transactionDate;
    private final LocalDateTime createdAt;

    public TransactionResponse(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.categoryId = transaction.getCategory().getId();
        this.categoryName = transaction.getCategory().getName();
        this.description = transaction.getDescription();
        this.transactionDate = transaction.getTransactionDate();
        this.createdAt = transaction.getCreatedAt();
    }
}

package org.example.moneylog.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.moneylog.domain.CategoryType;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class TransactionRequest {

    @NotNull(message = "거래 타입은 필수입니다.")
    private CategoryType type;

    @NotNull(message = "금액은 필수입니다.")
    @Positive(message = "금액은 0보다 커야 합니다.")
    private Long amount;

    @NotNull(message = "카테고리는 필수입니다.")
    private Long categoryId;

    private String description;

    @NotNull(message = "거래 날짜는 필수입니다.")
    private LocalDate transactionDate;
}

package org.example.moneylog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.moneylog.domain.User;
import org.example.moneylog.dto.*;
import org.example.moneylog.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ApiResponse<TransactionListResponse> getTransactions(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String yearMonth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        YearMonth targetMonth = (yearMonth != null) ? YearMonth.parse(yearMonth) : YearMonth.now();
        Page<TransactionResponse> result = transactionService.getMonthlyTransactions(
                user.getId(), targetMonth, PageRequest.of(page, size));

        TransactionListResponse data = new TransactionListResponse(result.getContent());
        return ApiResponse.successWithMeta("거래내역 목록을 조회했습니다.", data, ResponseMeta.fromPage(result));
    }

    @GetMapping("/{id}")
    public ApiResponse<TransactionResponse> getTransaction(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return ApiResponse.success("거래내역을 조회했습니다.", transactionService.getTransaction(user.getId(), id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionResponse>> createTransaction(
            @AuthenticationPrincipal User user, @Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.createTransaction(user, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("거래내역이 등록되었습니다.", response));
    }

    @PutMapping("/{id}")
    public ApiResponse<TransactionResponse> updateTransaction(
            @AuthenticationPrincipal User user, @PathVariable Long id, @Valid @RequestBody TransactionRequest request) {
        return ApiResponse.success("거래내역이 수정되었습니다.", transactionService.updateTransaction(user.getId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTransaction(@AuthenticationPrincipal User user, @PathVariable Long id) {
        transactionService.deleteTransaction(user.getId(), id);
        return ApiResponse.success("거래내역이 삭제되었습니다.", null);
    }
}

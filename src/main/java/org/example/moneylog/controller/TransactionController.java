package org.example.moneylog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.moneylog.domain.User;
import org.example.moneylog.dto.*;
import org.example.moneylog.repository.UserRepository;
import org.example.moneylog.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

// 지금은 로그인(JWT)이 아직 없어서 userId를 쿼리 파라미터로 받아 테스트합니다.
// 3일차에 Spring Security를 붙이면 로그인한 사용자 정보로 대체할 예정입니다.
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final UserRepository userRepository;

    @GetMapping
    public ApiResponse<TransactionListResponse> getTransactions(
            @RequestParam Long userId,
            @RequestParam(required = false) String yearMonth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        YearMonth targetMonth = (yearMonth != null) ? YearMonth.parse(yearMonth) : YearMonth.now();
        Page<TransactionResponse> result = transactionService.getMonthlyTransactions(
                userId, targetMonth, PageRequest.of(page, size));

        TransactionListResponse data = new TransactionListResponse(result.getContent());
        return ApiResponse.successWithMeta("거래내역 목록을 조회했습니다.", data, ResponseMeta.fromPage(result));
    }

    @GetMapping("/{id}")
    public ApiResponse<TransactionResponse> getTransaction(@RequestParam Long userId, @PathVariable Long id) {
        return ApiResponse.success("거래내역을 조회했습니다.", transactionService.getTransaction(userId, id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionResponse>> createTransaction(
            @RequestParam Long userId, @Valid @RequestBody TransactionRequest request) {
        User user = userRepository.findById(userId).orElseThrow();
        TransactionResponse response = transactionService.createTransaction(user, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("거래내역이 등록되었습니다.", response));
    }

    @PutMapping("/{id}")
    public ApiResponse<TransactionResponse> updateTransaction(
            @RequestParam Long userId, @PathVariable Long id, @Valid @RequestBody TransactionRequest request) {
        return ApiResponse.success("거래내역이 수정되었습니다.", transactionService.updateTransaction(userId, id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTransaction(@RequestParam Long userId, @PathVariable Long id) {
        transactionService.deleteTransaction(userId, id);
        return ApiResponse.success("거래내역이 삭제되었습니다.", null);
    }
}

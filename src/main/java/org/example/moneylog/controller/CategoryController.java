package org.example.moneylog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.moneylog.domain.User;
import org.example.moneylog.dto.ApiResponse;
import org.example.moneylog.dto.CategoryRequest;
import org.example.moneylog.dto.CategoryResponse;
import org.example.moneylog.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getCategories(@AuthenticationPrincipal User user) {
        return ApiResponse.success("카테고리 목록을 조회했습니다.", categoryService.getMyCategories(user.getId()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @AuthenticationPrincipal User user, @Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(user, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("카테고리가 추가되었습니다.", response));
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> updateCategory(
            @AuthenticationPrincipal User user, @PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        return ApiResponse.success("카테고리가 수정되었습니다.", categoryService.updateCategory(user.getId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@AuthenticationPrincipal User user, @PathVariable Long id) {
        categoryService.deleteCategory(user.getId(), id);
        return ApiResponse.success("카테고리가 삭제되었습니다.", null);
    }
}

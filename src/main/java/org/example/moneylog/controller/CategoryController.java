package org.example.moneylog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.moneylog.domain.User;
import org.example.moneylog.dto.ApiResponse;
import org.example.moneylog.dto.CategoryRequest;
import org.example.moneylog.dto.CategoryResponse;
import org.example.moneylog.repository.UserRepository;
import org.example.moneylog.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 지금은 로그인(JWT)이 아직 없어서 userId를 쿼리 파라미터로 받아 테스트합니다.
// 3일차에 Spring Security를 붙이면 로그인한 사용자 정보로 대체할 예정입니다.
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final UserRepository userRepository;

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getCategories(@RequestParam Long userId) {
        return ApiResponse.success("카테고리 목록을 조회했습니다.", categoryService.getMyCategories(userId));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @RequestParam Long userId, @Valid @RequestBody CategoryRequest request) {
        User user = userRepository.findById(userId).orElseThrow();
        CategoryResponse response = categoryService.createCategory(user, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("카테고리가 추가되었습니다.", response));
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> updateCategory(
            @RequestParam Long userId, @PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        return ApiResponse.success("카테고리가 수정되었습니다.", categoryService.updateCategory(userId, id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@RequestParam Long userId, @PathVariable Long id) {
        categoryService.deleteCategory(userId, id);
        return ApiResponse.success("카테고리가 삭제되었습니다.", null);
    }
}

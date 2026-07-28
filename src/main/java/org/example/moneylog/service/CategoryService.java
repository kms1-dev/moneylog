package org.example.moneylog.service;

import lombok.RequiredArgsConstructor;
import org.example.moneylog.domain.Category;
import org.example.moneylog.domain.CategoryType;
import org.example.moneylog.domain.User;
import org.example.moneylog.dto.CategoryRequest;
import org.example.moneylog.dto.CategoryResponse;
import org.example.moneylog.exception.CategoryInUseException;
import org.example.moneylog.exception.CategoryNotFoundException;
import org.example.moneylog.repository.CategoryRepository;
import org.example.moneylog.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    // 회원가입 시 기본 카테고리 자동 생성 (지출: 식비/교통/주거/문화, 수입: 급여/용돈)
    @Transactional
    public void createDefaultCategories(User user) {
        categoryRepository.save(new Category(user, "식비", CategoryType.EXPENSE));
        categoryRepository.save(new Category(user, "교통", CategoryType.EXPENSE));
        categoryRepository.save(new Category(user, "주거", CategoryType.EXPENSE));
        categoryRepository.save(new Category(user, "문화", CategoryType.EXPENSE));
        categoryRepository.save(new Category(user, "급여", CategoryType.INCOME));
        categoryRepository.save(new Category(user, "용돈", CategoryType.INCOME));
    }

    public List<CategoryResponse> getMyCategories(Long userId) {
        return categoryRepository.findByUserIdOrderByIdAsc(userId).stream()
                .map(CategoryResponse::new)
                .toList();
    }

    @Transactional
    public CategoryResponse createCategory(User user, CategoryRequest request) {
        Category category = new Category(user, request.getName(), request.getType());
        categoryRepository.save(category);
        return new CategoryResponse(category);
    }

    @Transactional
    public CategoryResponse updateCategory(Long userId, Long categoryId, CategoryRequest request) {
        Category category = categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(CategoryNotFoundException::new);
        category.update(request.getName(), request.getType());
        return new CategoryResponse(category);
    }

    @Transactional
    public void deleteCategory(Long userId, Long categoryId) {
        Category category = categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(CategoryNotFoundException::new);
        // 이 카테고리를 쓰는 거래가 남아있으면 DB에서 삭제가 막히기 때문에 미리 확인한다.
        if (transactionRepository.existsByCategoryId(categoryId)) {
            throw new CategoryInUseException();
        }
        categoryRepository.delete(category);
    }
}

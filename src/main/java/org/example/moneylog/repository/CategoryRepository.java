package org.example.moneylog.repository;

import org.example.moneylog.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserIdOrderByIdAsc(Long userId);
    Optional<Category> findByIdAndUserId(Long id, Long userId);
}

package org.example.moneylog.repository;

import org.example.moneylog.domain.CategoryType;
import org.example.moneylog.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByIdAndUserId(Long id, Long userId);

    Page<Transaction> findByUserIdAndTransactionDateBetweenOrderByTransactionDateDesc(
            Long userId, LocalDate from, LocalDate to, Pageable pageable);

    @Query("select coalesce(sum(t.amount), 0) from Transaction t " +
            "where t.user.id = :userId and t.type = :type and t.transactionDate between :from and :to")
    long sumAmount(@Param("userId") Long userId, @Param("type") CategoryType type,
                   @Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("select t.category.name as categoryName, sum(t.amount) as total from Transaction t " +
            "where t.user.id = :userId and t.type = :type and t.transactionDate between :from and :to " +
            "group by t.category.name")
    List<CategoryTotal> sumByCategory(@Param("userId") Long userId, @Param("type") CategoryType type,
                                       @Param("from") LocalDate from, @Param("to") LocalDate to);
}

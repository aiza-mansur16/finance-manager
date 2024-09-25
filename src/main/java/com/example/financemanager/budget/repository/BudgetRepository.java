package com.example.financemanager.budget.repository;

import com.example.financemanager.budget.model.BudgetEntity;
import com.example.financemanager.utils.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<BudgetEntity, Long> {
  Optional<BudgetEntity> findByUserIdAndMonthAndYearAndCategory(
      Long userId,
      Month month,
      Integer year,
      Category category
  );
}

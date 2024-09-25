package com.example.financemanager.expense.repository;

import com.example.financemanager.expense.model.ExpenseEntity;
import com.example.financemanager.utils.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Month;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {
  List<ExpenseEntity> findByUserIdAndMonthAndYearAndCategory(
      Long userId,
      Month month,
      Integer year,
      Category category
  );
}

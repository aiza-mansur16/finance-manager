package com.example.financemanager.expense.service;

import com.example.financemanager.expense.mapper.ExpenseMapper;
import com.example.financemanager.expense.model.ExpenseCreateDto;
import com.example.financemanager.expense.model.ExpenseDto;
import com.example.financemanager.expense.model.ExpenseEntity;
import com.example.financemanager.expense.model.ExpenseQueryDto;
import com.example.financemanager.expense.repository.ExpenseRepository;
import com.example.financemanager.tracker.service.ExpenseTrackerService;
import com.example.financemanager.utils.model.Page;
import com.example.financemanager.utils.model.ResponseEnvelope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ExpenseService {

  private final ExpenseRepository repository;
  private final ExpenseMapper mapper;

  private final ExpenseTrackerService expenseTrackerService;

  public ExpenseService(ExpenseRepository repository, ExpenseMapper mapper, ExpenseTrackerService expenseTrackerService) {
    this.repository = repository;
    this.mapper = mapper;
    this.expenseTrackerService = expenseTrackerService;
  }

  public ExpenseDto createExpense(ExpenseCreateDto expense) {
    if (log.isDebugEnabled()) {
      log.debug("Adding expense info for user:{}", expense.userId());
    }
    var expenseEntity = mapper.toExpenseEntity(expense);
    expenseTrackerService.track(expenseEntity);
    return mapper.toExpenseDto(repository.save(expenseEntity));
  }

  public ResponseEnvelope<List<ExpenseDto>> findAllExpenses(ExpenseQueryDto expenseQuery) {
    if (log.isDebugEnabled()) {
      log.debug("Finding all expense info with query:{}", expenseQuery);
    }
    var result = repository.findAll(getExampleExpenseEntity(expenseQuery),
        PageRequest.of(expenseQuery.page(), expenseQuery.size()));
    return new ResponseEnvelope<>(
        result.stream().map(mapper::toExpenseDto).toList(),
        null,
        new Page(expenseQuery.page(), expenseQuery.size(), result.getTotalElements(), result.getTotalPages()));

  }

  private Example<ExpenseEntity> getExampleExpenseEntity(ExpenseQueryDto expenseQuery) {
    return Example.of(ExpenseEntity
        .builder()
        .id(expenseQuery.id())
        .userId(expenseQuery.userId())
        .dayOfMonth(expenseQuery.dayOfMonth())
        .month(expenseQuery.month())
        .year(expenseQuery.year())
        .category(expenseQuery.category())
        .amount(expenseQuery.amount())
        .description(expenseQuery.description())
        .build());
  }

  public void deleteExpense(Long id) {
    if (log.isDebugEnabled()) {
      log.debug("Deleting expense with id:{}", id);
    }
    repository.deleteById(id);
  }

  public ExpenseDto findById(Long id) {
    return mapper.toExpenseDto(repository.getReferenceById(id));
  }
}

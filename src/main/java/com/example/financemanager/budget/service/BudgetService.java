package com.example.financemanager.budget.service;

import com.example.financemanager.budget.mapper.BudgetMapper;
import com.example.financemanager.budget.model.*;
import com.example.financemanager.budget.repository.BudgetRepository;
import com.example.financemanager.utils.model.Page;
import com.example.financemanager.utils.model.ResponseEnvelope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BudgetService {

  private final BudgetRepository repository;
  private final BudgetMapper mapper;

  public BudgetService(BudgetRepository repository, BudgetMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  public BudgetDto createBudget(BudgetCreateDto budget) {
    if (log.isDebugEnabled()) {
      log.debug("Creating budget for user:{}", budget.userId());
    }
    var budgetEntity = mapper.toBudgetEntity(budget);
    try {
      return mapper.toBudgetDto(repository.saveAndFlush(budgetEntity));
    } catch (DataIntegrityViolationException e) {
      if (log.isWarnEnabled()) {
        log.warn("Error saving budget:{}", e.getMessage());
      }
      throw new IllegalArgumentException("Budget already exists for this user with given month, year and category.", e);
    }
  }

  public ResponseEnvelope<List<BudgetDto>> findAllBudgets(BudgetQueryDto budgetQuery) {
    if (log.isDebugEnabled()) {
      log.debug("Getting all budgets for query:{}", budgetQuery);
    }
    var result = repository.findAll(getExampleBudgetEntity(budgetQuery),
        PageRequest.of(budgetQuery.page(), budgetQuery.size()));
    return new ResponseEnvelope<>(
        result.stream().map(mapper::toBudgetDto).toList(),
        null,
        new Page(budgetQuery.page(), budgetQuery.size(), result.getTotalElements(), result.getTotalPages()));
  }

  public BudgetDto patchBudget(Long id, BudgetPatchDto budget) {
    if (log.isDebugEnabled()) {
      log.debug("Retrieve budget to patch with id:{}", id);
    }
    var savedBudget = repository.getReferenceById(id);
    if (log.isDebugEnabled()) {
      log.debug("Patching budget with id:{}", id);
    }
    return mapper.toBudgetDto(repository.save(getPatchedBudget(budget, savedBudget)));
  }

  private Example<BudgetEntity> getExampleBudgetEntity(BudgetQueryDto budgetQuery) {
    return Example.of(BudgetEntity
        .builder()
        .id(budgetQuery.id())
        .userId(budgetQuery.userId())
        .month(budgetQuery.month())
        .year(budgetQuery.year())
        .category(budgetQuery.category())
        .limit(budgetQuery.limit())
        .description(budgetQuery.description())
        .build());
  }

  private BudgetEntity getPatchedBudget(BudgetPatchDto budgetPatchDto, BudgetEntity budgetEntity) {
    if (budgetPatchDto.description() != null) {
      budgetEntity.setDescription(budgetPatchDto.description());
    }

    if (budgetPatchDto.limit() != null) {
      budgetEntity.setLimit(budgetPatchDto.limit());
    }

    return budgetEntity;
  }

  public void deleteBudget(Long id) {
    if (log.isDebugEnabled()) {
      log.debug("Deleting budget with id:{}", id);
    }
    repository.deleteById(id);
  }

  public BudgetDto findById(Long id) {
    return mapper.toBudgetDto(repository.getReferenceById(id));
  }
}

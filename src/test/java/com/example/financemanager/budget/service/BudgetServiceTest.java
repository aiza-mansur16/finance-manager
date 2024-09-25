package com.example.financemanager.budget.service;

import com.example.financemanager.budget.model.BudgetCreateDto;
import com.example.financemanager.budget.model.BudgetEntity;
import com.example.financemanager.budget.model.BudgetPatchDto;
import com.example.financemanager.budget.model.BudgetQueryDto;
import com.example.financemanager.budget.repository.BudgetRepository;
import com.example.financemanager.utils.model.Category;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class BudgetServiceTest {

    @Autowired
    private BudgetService budgetService;
    @MockBean
    private BudgetRepository budgetRepository;


    @Test
    void createBudgetSuccess () {
        var budget = new BudgetCreateDto(
                1L,
                Category.GROCERY,
                BigDecimal.valueOf(10000.00),
                Month.JANUARY,
                2025,
                "Grocery Budget"
        );
        Mockito.when(budgetRepository.saveAndFlush(any(BudgetEntity.class)))
                .thenAnswer(it -> it.getArguments()[0]);

        var result = budgetService.createBudget(budget);

        verify(budgetRepository).saveAndFlush(any(BudgetEntity.class));
        assertEquals(1L, result.userId());
        assertEquals("Grocery Budget", result.description());
        assertEquals(Category.GROCERY, result.category());
        assertEquals(BigDecimal.valueOf(10000.00), result.limit());
        assertEquals(Month.JANUARY, result.month());
        assertEquals(2025, result.year());
    }

    @Test
    void createBudgetFailure () {
        var budget = new BudgetCreateDto(
                1L,
                Category.GROCERY,
                BigDecimal.valueOf(10000.00),
                Month.JANUARY,
                2025,
                null
        );
        Mockito.when(budgetRepository.saveAndFlush(any(BudgetEntity.class)))
                .thenThrow(new DataIntegrityViolationException("Already exists."));

        var exceptionThrown = assertThrows(IllegalArgumentException.class, () -> budgetService.createBudget(budget));

        assertEquals("Budget already exists for this user with given month, year and category.",
                exceptionThrown.getMessage());
    }

    @Test
    void findAllBudgetsSuccess () {
        var budgetEntity = new BudgetEntity();
        budgetEntity.setId(1L);
        budgetEntity.setUserId(1L);
        budgetEntity.setMonth(Month.JANUARY);
        budgetEntity.setYear(2025);
        budgetEntity.setCategory(Category.GROCERY);
        budgetEntity.setLimit(BigDecimal.valueOf(10000.00));
        budgetEntity.setDescription("Grocery Budget");
        var budgetEntityPage = new PageImpl<>(List.of(budgetEntity),
                PageRequest.of(0, 10), 1);
        Mockito.when(budgetRepository.findAll(any(Example.class), any(Pageable.class)))
               .thenReturn(budgetEntityPage);

        var result = budgetService.findAllBudgets(new BudgetQueryDto(
                1L,
                1L,
                null,
                null,
                Month.JANUARY,
                null,
                null,
                1,
                10));

        assertNotNull(result);
        assertNotNull(result.data());
        assertEquals(1, result.data().size());
    }

    @Test
    void patchBudgetSuccess () {
        var budgetEntity = new BudgetEntity();
        budgetEntity.setId(1L);
        budgetEntity.setUserId(1L);
        budgetEntity.setMonth(Month.JANUARY);
        budgetEntity.setYear(2025);
        budgetEntity.setCategory(Category.GROCERY);
        budgetEntity.setLimit(BigDecimal.valueOf(10000.00));
        budgetEntity.setDescription("Grocery Budget");
        Mockito.when(budgetRepository.getReferenceById(1L)).thenReturn(budgetEntity);
        var budgetPatch = new BudgetPatchDto(BigDecimal.valueOf(15000.00), null);
        Mockito.when(budgetRepository.save(any())).thenAnswer(it -> it.getArguments()[0]);

        var result = budgetService.patchBudget(1L, budgetPatch);

        verify(budgetRepository).save(budgetEntity);
        assertEquals(1L, result.userId());
        assertEquals("Grocery Budget", result.description());
        assertEquals(Category.GROCERY, result.category());
        assertEquals(BigDecimal.valueOf(15000.00), result.limit());
        assertEquals(Month.JANUARY, result.month());
        assertEquals(2025, result.year());
    }

    @Test
    void patchBudgetFailure() {
        Mockito.when(budgetRepository.getReferenceById(1L)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> budgetService.patchBudget(1L,  new BudgetPatchDto(BigDecimal.valueOf(15000.00), null)));

    }

    @Test
    void deleteBudgetSuccess () {
        budgetService.deleteBudget(1L);

        verify(budgetRepository).deleteById(1L);
    }

    @Test
    void findByIdSuccess () {
        var budgetEntity = new BudgetEntity();
        budgetEntity.setId(1L);
        budgetEntity.setUserId(1L);
        budgetEntity.setMonth(Month.JANUARY);
        budgetEntity.setYear(2025);
        budgetEntity.setCategory(Category.GROCERY);
        budgetEntity.setLimit(BigDecimal.valueOf(10000.00));
        budgetEntity.setDescription("Grocery Budget");
        Mockito.when(budgetRepository.getReferenceById(1L)).thenReturn(budgetEntity);

        var result = budgetService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.userId());
        assertEquals("Grocery Budget", result.description());
        assertEquals(Category.GROCERY, result.category());
        assertEquals(BigDecimal.valueOf(10000.00), result.limit());
    }

    @Test
    void findByIdFailure () {
        Mockito.when(budgetRepository.getReferenceById(1L)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> budgetService.findById(1L));
    }
}

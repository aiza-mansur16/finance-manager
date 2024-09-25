package com.example.financemanager.expense.service;

import com.example.financemanager.expense.model.ExpenseCreateDto;
import com.example.financemanager.expense.model.ExpenseEntity;
import com.example.financemanager.expense.model.ExpenseQueryDto;
import com.example.financemanager.expense.repository.ExpenseRepository;
import com.example.financemanager.tracker.service.ExpenseTrackerService;
import com.example.financemanager.utils.model.Category;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("test")
class ExpenseServiceTest {

    @Autowired
    private ExpenseService expenseService;

    @MockBean
    private ExpenseTrackerService expenseTrackerService;

    @MockBean
    private ExpenseRepository expenseRepository;

    @Test
    void testCreateExpenseSuccess() {
        Mockito.doNothing().when(expenseTrackerService).track(any());
        Mockito.when(expenseRepository.save(any())).thenAnswer(it -> it.getArguments()[0]);

        var result = expenseService.createExpense(new ExpenseCreateDto(
                1L,
                BigDecimal.valueOf(1000.00),
                Category.GROCERY,
                LocalDate.of(2025, Month.JANUARY, 10),
                "Grocery purchase"
        ));

        Mockito.verify(expenseTrackerService, Mockito.times(1)).track(any());
        Mockito.verify(expenseRepository, Mockito.times(1)).save(any());
        assertNotNull(result);
        assertEquals(1, result.userId());
        assertEquals(Category.GROCERY, result.category());
        assertEquals(LocalDate.of(2025, Month.JANUARY, 10), result.date());
        assertEquals("Grocery purchase", result.description());
        assertEquals(BigDecimal.valueOf(1000.00), result.amount());
    }

    @Test
    void findAllExpensesSuccess() {
        var expenseEntity = new ExpenseEntity();
        expenseEntity.setId(1L);
        expenseEntity.setUserId(1L);
        expenseEntity.setDayOfMonth(25);
        expenseEntity.setMonth(Month.JANUARY);
        expenseEntity.setYear(2025);
        expenseEntity.setCategory(Category.GROCERY);
        expenseEntity.setAmount(BigDecimal.valueOf(10000.00));
        expenseEntity.setDescription("Grocery Expense");
        var expenseEntityPage = new PageImpl<>(List.of(expenseEntity),
                PageRequest.of(0, 10), 1);
        Mockito.when(expenseRepository.findAll(any(Example.class), any(Pageable.class)))
                .thenReturn(expenseEntityPage);

        var result = expenseService.findAllExpenses(new ExpenseQueryDto(
                1L,
                1L,
                null,
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
    void deleteExpenseSuccess() {
        var expenseEntity = new ExpenseEntity();
        expenseEntity.setId(1L);
        expenseEntity.setUserId(1L);
        expenseEntity.setDayOfMonth(25);
        expenseEntity.setMonth(Month.JANUARY);
        expenseEntity.setYear(2025);
        expenseEntity.setCategory(Category.GROCERY);
        expenseEntity.setAmount(BigDecimal.valueOf(10000.00));
        expenseEntity.setDescription("Grocery Expense");

        expenseService.deleteExpense(1L);

        Mockito.verify(expenseRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void findByIdSuccess() {
        var expenseEntity = new ExpenseEntity();
        expenseEntity.setId(1L);
        expenseEntity.setUserId(1L);
        expenseEntity.setDayOfMonth(25);
        expenseEntity.setMonth(Month.JANUARY);
        expenseEntity.setYear(2025);
        expenseEntity.setCategory(Category.GROCERY);
        expenseEntity.setAmount(BigDecimal.valueOf(10000.00));
        expenseEntity.setDescription("Grocery Expense");
        Mockito.when(expenseRepository.getReferenceById(1L)).thenReturn(expenseEntity);

        var result = expenseService.findById(1L);

        assertNotNull(result);
        assertEquals(expenseEntity.getId(), result.id());
    }

    @Test
    void findByIdFailure() {
        Mockito.when(expenseRepository.getReferenceById(1L)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> expenseService.findById(1L));
    }
}

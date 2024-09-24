package com.example.financemanager.tracker.service;

import com.example.financemanager.adapter.UserAdapter;
import com.example.financemanager.budget.model.BudgetEntity;
import com.example.financemanager.budget.repository.BudgetRepository;
import com.example.financemanager.expense.model.ExpenseEntity;
import com.example.financemanager.expense.repository.ExpenseRepository;
import com.example.financemanager.notification.service.EmailNotificationService;
import com.example.financemanager.utils.model.Category;
import com.example.financemanager.utils.model.UserDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("test")
class ExpenseTrackerServiceTest {

    @Autowired
    private ExpenseTrackerService expenseTrackerService;

    @MockBean
    private ExpenseRepository expenseRepository;

    @MockBean
    private BudgetRepository budgetRepository;

    @MockBean
    private UserAdapter userAdapter;

    @MockBean
    private EmailNotificationService emailNotificationService;

    @Test
    void testTrackExpenseSuccess() {
        // Given
        var expenseEntity = new ExpenseEntity();
        expenseEntity.setId(1L);
        expenseEntity.setUserId(1L);
        expenseEntity.setDayOfMonth(25);
        expenseEntity.setMonth(Month.JANUARY);
        expenseEntity.setYear(2025);
        expenseEntity.setCategory(Category.GROCERY);
        expenseEntity.setAmount(BigDecimal.valueOf(20000.00));
        expenseEntity.setDescription("Grocery Expense");
        Mockito.when(expenseRepository.findByUserIdAndMonthAndYearAndCategory(
                any(),
                any(),
                any(),
                any()
        )).thenReturn(List.of(expenseEntity));

        var budgetEntity = new BudgetEntity();
        budgetEntity.setId(1L);
        budgetEntity.setUserId(1L);
        budgetEntity.setMonth(Month.JANUARY);
        budgetEntity.setYear(2025);
        budgetEntity.setCategory(Category.GROCERY);
        budgetEntity.setLimit(BigDecimal.valueOf(15000.00));
        budgetEntity.setDescription("Grocery Budget");

        Mockito.when(budgetRepository.findByUserIdAndMonthAndYearAndCategory(
                any(),
                any(),
                any(),
                any()
        )).thenReturn(Optional.of(budgetEntity));
        Mockito.when(userAdapter.getInfo(any())).thenReturn(new UserDto(
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                "John Doe"
        ));

        // When
        expenseTrackerService.track(expenseEntity);

        // Then
        Mockito.verify(emailNotificationService, Mockito.times(1)).notify(any());
    }
}

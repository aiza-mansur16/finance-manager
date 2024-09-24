package com.example.financemanager.tracker.service;

import com.example.financemanager.adapter.UserAdapter;
import com.example.financemanager.budget.model.BudgetEntity;
import com.example.financemanager.budget.repository.BudgetRepository;
import com.example.financemanager.expense.model.ExpenseEntity;
import com.example.financemanager.expense.repository.ExpenseRepository;
import com.example.financemanager.notification.service.EmailNotificationService;
import com.example.financemanager.utils.model.EmailInfoDto;
import com.example.financemanager.utils.model.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class ExpenseTrackerService implements Tracker<ExpenseEntity> {

    private final ExpenseRepository expenseRepository;

    private final BudgetRepository budgetRepository;

    private final UserAdapter userAdapter;

    private final EmailNotificationService emailNotificationService;

    @Value("${user.api.base-url}")
    private String userApiBaseUrl;

    public ExpenseTrackerService(ExpenseRepository expenseRepository,
                                 BudgetRepository budgetRepository,
                                 UserAdapter userAdapter, EmailNotificationService emailNotificationService) {
        this.expenseRepository = expenseRepository;
        this.budgetRepository = budgetRepository;
        this.userAdapter = userAdapter;
        this.emailNotificationService = emailNotificationService;
    }

    @Override
    public void track(ExpenseEntity expenseInfo) {
        if (log.isDebugEnabled()) {
            log.debug("Tracking expense for user: {}", expenseInfo.getUserId());
        }
        budgetRepository.findByUserIdAndMonthAndYearAndCategory(
                        expenseInfo.getUserId(),
                        expenseInfo.getMonth(),
                        expenseInfo.getYear(),
                        expenseInfo.getCategory())
                .ifPresent(
                        budget -> {
                            var expenses = expenseRepository.findByUserIdAndMonthAndYearAndCategory(
                                    expenseInfo.getUserId(),
                                    expenseInfo.getMonth(),
                                    expenseInfo.getYear(),
                                    expenseInfo.getCategory());
                            var totalExpenses = expenses.stream().map(ExpenseEntity::getAmount)
                                    .reduce(expenseInfo.getAmount(), BigDecimal::add);
                            if (totalExpenses.compareTo(budget.getLimit()) > 0) {
                                if (log.isDebugEnabled()) {
                                    log.debug("Total expense:{} of user:{} for month:{}, year:{} and category:{} has " +
                                                    "exceeded the budget limit:{}",
                                            totalExpenses,
                                            expenseInfo.getUserId(),
                                            expenseInfo.getMonth(),
                                            budget.getYear(),
                                            budget.getCategory(),
                                            budget.getLimit());
                                }
                                var user = fetchUserInfo(expenseInfo.getUserId());
                                var exceededAmount = totalExpenses.subtract(expenseInfo.getAmount());
                                var notification = createEmailNotification(user, budget, expenseInfo, exceededAmount);
                                emailNotificationService.notify(notification);
                            }
                        }
                );
    }


    private UserDto fetchUserInfo(Long userId) {
        return userAdapter.getInfo(userApiBaseUrl + "/" + userId);
    }

    private EmailInfoDto createEmailNotification(UserDto user, BudgetEntity budget, ExpenseEntity expenseInfo,
                                                 BigDecimal exceededAmount) {
        var message = """
                To %s,
                                
                Your expenses for %s %s and category %s have exceeded the set budget limit by %s.
                Details of budget and recent expense are as follows:
                Budget Category: %s
                Budget Amount: %s
                Expense Description: %s
                Expense Amount: %s
                                
                Regards,
                Finance Manager App""";
        return new EmailInfoDto(user.email(), "Budget Exceed Alert",
                String.format(message,
                        user.firstName(),
                        budget.getMonth(),
                        budget.getYear(),
                        budget.getCategory(),
                        exceededAmount,
                        budget.getCategory(),
                        budget.getLimit(),
                        expenseInfo.getDescription(),
                        expenseInfo.getAmount()));
    }

}

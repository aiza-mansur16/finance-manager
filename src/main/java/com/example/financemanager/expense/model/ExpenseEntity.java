package com.example.financemanager.expense.model;

import com.example.financemanager.utils.model.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Month;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "expense_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "expense_amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "expense_month", nullable = false)
    @Enumerated(EnumType.STRING)
    private Month month;

    @Column(name = "expense_year", nullable = false)
    private Integer year;

    @Column(name = "expense_day_of_month", nullable = false)
    private Integer dayOfMonth;

    @Column(name = "description", nullable = false)
    private String description;
}

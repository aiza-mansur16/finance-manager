package com.example.financemanager.budget.model;

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
@Entity(name = "budget")
@Table(
        name="budget",
        uniqueConstraints=
        @UniqueConstraint(name = "monthlyUserBudgetConstraint",
                columnNames={"user_id", "budget_month", "budget_year", "budget_category"})
)
public class BudgetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "budget_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "budget_limit", nullable = false)
    private BigDecimal limit;

    @Column(name = "budget_month", nullable = false)
    @Enumerated(EnumType.STRING)
    private Month month;

    @Column(name = "budget_year", nullable = false)
    private Integer year;

    @Column(name = "description")
    private String description;
}

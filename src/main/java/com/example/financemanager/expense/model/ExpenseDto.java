package com.example.financemanager.expense.model;

import com.example.financemanager.utils.model.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseDto(
        @Schema(description = "id of expense")
        @JsonProperty("id")
        Long id,

        @Schema(description = "user id to whom the expense belongs")
        @JsonProperty("userId")
        Long userId,

        @Schema(description = "amount of expense")
        @JsonProperty("amount")
        BigDecimal amount,

        @Schema(description = "category of expense")
        @JsonProperty("category")
        Category category,

        @Schema(description = "date of expense")
        @JsonProperty("date")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @Schema(description = "description of expense")
        @JsonProperty("description")
        String description
) {
}

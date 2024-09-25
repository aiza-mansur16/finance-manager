package com.example.financemanager.expense.model;

import com.example.financemanager.utils.model.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Month;

public record ExpenseQueryDto(
        @Schema(description = "id of expense")
        @JsonProperty("id")
        Long id,
        @Schema(description = "id of user to whom expense belongs")
        @JsonProperty("userId")
        Long userId,
        @Schema(description = "category of expense")
        @JsonProperty("category")
        Category category,
        @Schema(description = "amount of expense")
        @JsonProperty("amount")
        BigDecimal amount,
        @Schema(description = "day of month when expense was created")
        @JsonProperty("dayOfMonth")
        Integer dayOfMonth,
        @Schema(description = "mont of expense")
        @JsonProperty("month")
        Month month,
        @Schema(description = "year of expense")
        @JsonProperty("year")
        Integer year,
        @Schema(description = "description of expense")
        @JsonProperty("description")
        String description,
        @Schema(description = "page number")
        @JsonProperty("page")
        @Min(value = 0, message = "page cannot be negative")
        @NotNull
        Integer page,
        @Schema(description = "page size")
        @JsonProperty("size")
        @Min(1)
        @Max(50)
        @NotNull
        Integer size
) {
}

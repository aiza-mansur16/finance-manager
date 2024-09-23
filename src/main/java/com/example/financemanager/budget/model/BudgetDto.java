package com.example.financemanager.budget.model;

import com.example.financemanager.utils.model.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.time.Month;

public record BudgetDto(
        @Schema(description = "id of budget")
        @JsonProperty("id")
        Long id,

        @Schema(description = "id of user to whom the budget belongs")
        @JsonProperty("userId")
        Long userId,

        @Schema(description = "budget category")
        @JsonProperty("category")
        Category category,

        @Schema(description = "budget limit")
        @Min(value = 0, message = "Budget limit must be positive")
        @JsonProperty("limit")
        BigDecimal limit,

        @Schema(description = "budget month")
        @JsonProperty("month")
        Month month,

        @Schema(description = "budget year")
        @JsonProperty("year")
        Integer year,

        @Schema(description = "budget description")
        @JsonProperty("description")
        String description
) {
}

package com.example.financemanager.budget.model;

import com.example.financemanager.utils.model.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Month;

public record BudgetCreateDto(
        @Schema(description = "user id for whom budget is to be created")
        @JsonProperty("userId")
        @NotNull(message = "user id cannot be null")
        Long userId,

        @Schema(description = "budget category")
        @JsonProperty("category")
        @NotNull(message = "category cannot be null")
        Category category,

        @Schema(description = "budget limit")
        @JsonProperty("limit")
        @NotNull(message = "limit cannot be null")
        @Min(value = 10, message = "budget limit cannot be less than 10")
        BigDecimal limit,

        @Schema(description = "budget month")
        @JsonProperty("month")
        @NotNull(message = "month cannot be null")
        Month month,

        @Schema(description = "budget year")
        @JsonProperty("year")
        @NotNull(message = "year cannot be null")
        Integer year,

        @Schema(description = "budget description")
        @JsonProperty("description")
        String description
) {
}

package com.example.financemanager.budget.model;

import com.example.financemanager.utils.model.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Month;

public record BudgetQueryDto(

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
        String description,
        @Schema(description = "page number")
        @JsonProperty("page")
        @Min(value = 0, message = "page cannot be negative")
        @NotNull(message = "page cannot be null")
        Integer page,
        @Schema(description = "page size")
        @JsonProperty("size")
        @Min(value = 1, message = "page size cannot be less than 1")
        @Max(value = 50, message = "page size cannot be greater than 50")
        @NotNull(message = "size cannot be null")
        Integer size
) {
}

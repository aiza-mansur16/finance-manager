package com.example.financemanager.budget.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record BudgetPatchDto(
        @Schema(description = "budget limit")
        @Min(value = 10, message = "budget limit cannot be less than 10")
        @JsonProperty("limit")
        BigDecimal limit,
        @Schema(description = "budget description")
        @JsonProperty("description")
        String description
) {
}

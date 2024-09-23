package com.example.financemanager.expense.model;

import com.example.financemanager.utils.model.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseCreateDto(
        @Schema(description = "user id for which expense is to be created")
        @JsonProperty("userId")
        @NotNull(message = "user id cannot be null")
        Long userId,

        @Schema(description = "amount of expense")
        @JsonProperty("amount")
        @NotNull(message = "amount cannot be null")
        @Min(value = 1, message = "amount cannot be less than 1")
        BigDecimal amount,

        @Schema(description = "category of expense")
        @JsonProperty("category")
        @NotNull(message = "category cannot be null")
        Category category,

        @Schema(description = "date of expense")
        @JsonProperty("date")
        @NotNull(message = "date cannot be null")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @Schema(description = "description of expense")
        @JsonProperty("description")
        @NotNull(message = "description cannot be null")
        @NotBlank(message = "description cannot be empty")
        String description

) {
}

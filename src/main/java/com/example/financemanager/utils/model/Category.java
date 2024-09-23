package com.example.financemanager.utils.model;

import io.swagger.v3.oas.annotations.media.Schema;

public enum Category {
    @Schema(description = "grocery category")
    GROCERY,
    @Schema(description = "transportation category")
    TRANSPORTATION,
    @Schema(description = "food category")
    FOOD,
    @Schema(description = "other items category")
    OTHERS
}

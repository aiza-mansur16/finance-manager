package com.example.financemanager.utils.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record Page(
        @Schema(description = "page number")
        long pageNumber,
        @Schema(description = "page size")
        long pageSize,
        @Schema(description = "total records")
        long totalRecords,
        @Schema(description = "total pages")
        long totalPages
) {
}

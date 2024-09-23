package com.example.financemanager.utils.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record ResponseEnvelope<T>(
        @Schema(description = "response data")
        T data,
        @Schema(description = "response error")
        Error errorInfo,
        @Schema(description = "response pagination information")
        Page page

) {
}

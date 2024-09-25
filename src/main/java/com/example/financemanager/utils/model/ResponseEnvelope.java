package com.example.financemanager.utils.model;

import io.swagger.v3.oas.annotations.media.Schema;

@edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = {
        "EI_EXPOSE_REP",
        "EI_EXPOSE_REP2"
}, justification = "Response Envelope used for responses and do not expose any sensitive information.")
public record ResponseEnvelope<T>(
        @Schema(description = "response data")
        T data,
        @Schema(description = "response error")
        Error errorInfo,
        @Schema(description = "response pagination information")
        Page page

) {
}

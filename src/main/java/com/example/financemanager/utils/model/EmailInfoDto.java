package com.example.financemanager.utils.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EmailInfoDto(
        @JsonProperty("recipientEmail")
        String recipientEmail,

        @JsonProperty("subject")
        String subject,

        @JsonProperty("message")
        String message
) {
}

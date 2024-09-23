package com.example.financemanager.utils.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
public class Error {
        @Schema(description = "status code of the error")
        private HttpStatus statusCode;

        @Schema(description = "messages of the error")
        private List<String> messages;
}

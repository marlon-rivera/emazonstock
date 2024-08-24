package com.emazon.stock.configuration.exceptionhandler;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ValidationExceptionResponse {
    private final List<String> errors;
    private final String status;
    private final LocalDateTime timestamp;

    public ValidationExceptionResponse(List<String> errors, String status, LocalDateTime timestamp) {
        this.errors = errors;
        this.status = status;
        this.timestamp = timestamp;
    }
}
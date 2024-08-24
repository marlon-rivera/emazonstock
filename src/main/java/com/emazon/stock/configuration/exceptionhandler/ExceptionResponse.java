package com.emazon.stock.configuration.exceptionhandler;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExceptionResponse {

    private final String message;
    private final String status;
    private final LocalDateTime localDateTime;

    public ExceptionResponse(String message, String status, LocalDateTime localDateTime) {
        this.message = message;
        this.status = status;
        this.localDateTime = localDateTime;
    }

}
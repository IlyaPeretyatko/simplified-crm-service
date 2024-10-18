package ru.peretyatko.app.util;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionErrorResponse {
    private String message;
    private LocalDateTime timestamp;

    public TransactionErrorResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}

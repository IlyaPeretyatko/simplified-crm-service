package ru.peretyatko.app.util;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SellerErrorResponse {
    private String message;
    private LocalDateTime timestamp;

    public SellerErrorResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

}

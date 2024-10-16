package ru.peretyatko.app.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerErrorResponse {
    private String message;
    private long timestamp;

    public SellerErrorResponse(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

}
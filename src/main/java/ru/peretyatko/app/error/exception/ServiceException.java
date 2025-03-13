package ru.peretyatko.app.error.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceException extends RuntimeException {

    private int code;

    private String message;

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}

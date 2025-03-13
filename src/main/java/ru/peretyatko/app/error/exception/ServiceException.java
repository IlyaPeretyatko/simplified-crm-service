package ru.peretyatko.app.error.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ServiceException extends RuntimeException {

    private int code;

    private String message;

    public ServiceException(HttpStatus code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}

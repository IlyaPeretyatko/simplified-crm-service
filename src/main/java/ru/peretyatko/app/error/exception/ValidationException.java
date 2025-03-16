package ru.peretyatko.app.error.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ValidationException extends RuntimeException {

    private String message;

    private Set<String> fieldsWithError;

    public ValidationException(String message, List<String> fieldsWithError) {
        this.message = message;
        this.fieldsWithError = new HashSet<>(fieldsWithError);
    }

}

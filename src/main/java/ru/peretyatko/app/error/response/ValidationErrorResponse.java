package ru.peretyatko.app.error.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class ValidationErrorResponse {

    private String message;

    private Set<String> fieldsWithError;

}

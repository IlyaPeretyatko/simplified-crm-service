package ru.peretyatko.app.validator.transaction;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.peretyatko.app.dto.transaction.TransactionPatchRequest;
import ru.peretyatko.app.dto.transaction.TransactionPostRequest;
import ru.peretyatko.app.dto.transaction.TransactionResponse;
import ru.peretyatko.app.validator.DefaultValidator;

@Component
public class TransactionValidatorImp extends DefaultValidator implements TransactionValidator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(TransactionResponse.class) || clazz.equals(TransactionPatchRequest.class) || clazz.equals(TransactionPostRequest.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        createAndThrowException(errors);
    }
}

package ru.peretyatko.app.validator.seller;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.peretyatko.app.dto.seller.SellerPatchRequest;
import ru.peretyatko.app.dto.seller.SellerResponse;
import ru.peretyatko.app.validator.DefaultValidator;

@Component
public class SellerValidatorImp extends DefaultValidator implements SellerValidator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(SellerPatchRequest.class) || clazz.equals(SellerResponse.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        createAndThrowException(errors);
    }
}

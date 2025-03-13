package ru.peretyatko.app.dto.transaction;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.peretyatko.app.model.PaymentType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionPatchRequest {
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    private double amount;

    private PaymentType paymentType;
}

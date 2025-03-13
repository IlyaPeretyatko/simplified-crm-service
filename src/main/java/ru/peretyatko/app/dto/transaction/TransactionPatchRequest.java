package ru.peretyatko.app.dto.transaction;

import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^(\\d+\\.\\d+|\\d+)$", message = "Amount must be a decimal.")
    private double amount;

    private PaymentType paymentType;
}

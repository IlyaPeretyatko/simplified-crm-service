package ru.peretyatko.app.dto.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.peretyatko.app.model.PaymentType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionPostRequest {
    @NotNull(message = "Seller id cannot be null.")
    private long sellerId;

    @NotNull(message = "Amount cannot be null.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    private double amount;

    @NotNull(message = "Payment type cannot be null.")
    private PaymentType paymentType;
}

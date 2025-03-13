package ru.peretyatko.app.dto.transaction;

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
    private long sellerId;

    private double amount;

    private PaymentType paymentType;
}

package ru.peretyatko.app.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.peretyatko.app.model.PaymentType;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private long id;

    private long sellerId;

    private double amount;

    private PaymentType paymentType;

    private LocalDateTime transactionDate;
}

package ru.peretyatko.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "Transactions")
public class Transaction {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private Seller seller;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "payment_type", nullable = false)
    private String paymentType;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    public Transaction() {}

    public Transaction(Seller seller, int amount, String paymentType, LocalDateTime transactionDate) {
        this.seller = seller;
        this.amount = amount;
        this.paymentType = paymentType;
        this.transactionDate = transactionDate;
    }

}

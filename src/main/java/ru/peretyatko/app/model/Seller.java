package ru.peretyatko.app.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "sellers")
public class Seller {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "contact_info")
    private String contactInfo;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    public Seller() {}

    public Seller(String name,String contactInfo, LocalDateTime registrationDate) {
        this.contactInfo = contactInfo;
        this.name = name;
        this.registrationDate = registrationDate;
        transactions = new ArrayList<>();
    }

}

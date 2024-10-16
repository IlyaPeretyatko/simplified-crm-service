package ru.peretyatko.app.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Entity
@Table(name = "Sellers")
public class Seller {

    @Setter
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Setter
    @Column(name = "contact_info")
    private String contactInfo;

    @Setter
    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;

    public Seller() {}

    public Seller(String name,String contactInfo, LocalDateTime registrationDate) {
        this.contactInfo = contactInfo;
        this.name = name;
        this.registrationDate = registrationDate;
    }

}

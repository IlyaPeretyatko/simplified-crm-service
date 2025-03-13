package ru.peretyatko.app.dto.seller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellerResponse {
    private long id;

    private String name;

    private String contactInfo;

    private LocalDateTime registrationDate;

}

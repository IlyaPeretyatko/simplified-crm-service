package ru.peretyatko.app.dto.seller;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellerPatchRequest {

    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters long.")
    private String name;

    @Size(min = 2, max = 50, message = "Contact info must be between 2 and 50 characters long.")
    private String contactInfo;
}

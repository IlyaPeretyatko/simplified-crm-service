package ru.peretyatko.app.dto.seller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SellerPostRequest {
    @NotNull(message = "Name cannot be null.")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters long.")
    private String name;

    @NotNull(message = "Contact info cannot be null.")
    @Size(min = 2, max = 50, message = "Contact info must be between 2 and 50 characters long.")
    private String contactInfo;
}

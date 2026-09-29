package com.tenantcare.backend.property.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PropertyRequest(
    @NotBlank(message = "Property name is required")
    String name,

    @NotBlank(message = "Adress is required")
    String adress,

    @NotNull(message = "Rent amount is required")
    @Positive(message = "Rent amount must be greater than zero")
    BigDecimal rentAmount,

    @NotNull(message = "Area is required")
    @Positive(message = "Area must be greater than zero")
    Double area
    ) {
}

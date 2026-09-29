package com.tenantcare.backend.property.dto;

import java.math.BigDecimal;

public record PropertyResponse(
    Long id,
    String name,
    String adress,
    BigDecimal rentAmount,
    Double area
) {
}

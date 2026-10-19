package com.tenantcare.backend.tenant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TenantRequest(
        @NotBlank(message = "First name is required") String firstName,
        @NotBlank(message = "Last name is required") String lastName,
        @NotBlank @Email(message = "Invalid email format") String email,
        String phoneNumber,
        @NotNull(message = "Property ID is required") Long propertyId
) {}

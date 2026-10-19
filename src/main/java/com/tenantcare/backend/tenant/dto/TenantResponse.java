package com.tenantcare.backend.tenant.dto;

public record TenantResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Long propertyId
) {}

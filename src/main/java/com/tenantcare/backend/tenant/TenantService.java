package com.tenantcare.backend.tenant;

import com.tenantcare.backend.property.Property;
import com.tenantcare.backend.property.PropertyRepository;
import com.tenantcare.backend.tenant.dto.TenantRequest;
import com.tenantcare.backend.tenant.dto.TenantResponse;
import com.tenantcare.backend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TenantService {
    private final TenantRepository tenantRepository;
    private final PropertyRepository propertyRepository;

    @Transactional
    public TenantResponse createTenant(TenantRequest request, User currentUser) {
        if (tenantRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Tenant with this email already exists");
        }

        if(tenantRepository.existsByPropertyId(request.propertyId())) {
            throw new IllegalArgumentException("This property already has a tenant");
        }

        Property property = propertyRepository.findByIdAndOwnerId(request.propertyId(), currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("Property not found or access denied"));

        Tenant tenant = Tenant.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .property(property)
                .build();

        Tenant savedTenant = tenantRepository.save(tenant);

        return mapToResponse(savedTenant);
    }

    private TenantResponse mapToResponse(Tenant tenant) {
        return new TenantResponse(
                tenant.getId(),
                tenant.getFirstName(),
                tenant.getLastName(),
                tenant.getEmail(),
                tenant.getPhoneNumber(),
                tenant.getProperty().getId()
        );
    }
}

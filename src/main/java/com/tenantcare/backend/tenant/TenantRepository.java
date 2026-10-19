package com.tenantcare.backend.tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    boolean existsByEmail(String email);
    boolean existsByPropertyId(Long propertyId);
}

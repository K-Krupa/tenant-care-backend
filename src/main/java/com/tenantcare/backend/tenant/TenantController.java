package com.tenantcare.backend.tenant;

import com.tenantcare.backend.tenant.dto.TenantRequest;
import com.tenantcare.backend.tenant.dto.TenantResponse;
import com.tenantcare.backend.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tenants")
@RequiredArgsConstructor
public class TenantController {
    private final TenantService tenantService;

    @PostMapping
    public ResponseEntity<TenantResponse> createTenant(
            @Valid @RequestBody TenantRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        TenantResponse response = tenantService.createTenant(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantResponse> getTenantById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        TenantResponse response = tenantService.getTenantById(id, currentUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTenant(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        tenantService.deleteTenant(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}

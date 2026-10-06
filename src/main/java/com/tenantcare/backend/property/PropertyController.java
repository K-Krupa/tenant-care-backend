package com.tenantcare.backend.property;

import com.tenantcare.backend.property.dto.PropertyRequest;
import com.tenantcare.backend.property.dto.PropertyResponse;
import com.tenantcare.backend.user.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class PropertyController {
    private final PropertyService propertyService;

    @PostMapping
    public ResponseEntity<PropertyResponse> createProperty(
            @Valid @RequestBody PropertyRequest request,
            @AuthenticationPrincipal User owner
    ) {
        PropertyResponse response = propertyService.createProperty(request, owner);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PropertyResponse>> getMyProperties(
            @AuthenticationPrincipal User owner
    ) {
        return ResponseEntity.ok(propertyService.getMyProperties(owner));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyResponse> updateProperty(
            @PathVariable Long id,
            @Valid @RequestBody PropertyRequest request,
            @AuthenticationPrincipal User owner
    ) {
        PropertyResponse response = propertyService.updateProperty(id, request, owner);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(
            @PathVariable Long id,
            @AuthenticationPrincipal User owner
    ) {
        propertyService.deleteProperty(id, owner);
        return ResponseEntity.noContent().build();
    }
}

package com.tenantcare.backend.property;

import com.tenantcare.backend.property.dto.PropertyRequest;
import com.tenantcare.backend.property.dto.PropertyResponse;
import com.tenantcare.backend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyResponse createProperty(PropertyRequest request, User owner) {
        Property property = Property.builder()
                .name(request.name())
                .address(request.address())
                .rentAmount(request.rentAmount())
                .area(request.area())
                .owner(owner)
                .build();

        Property savedProperty = propertyRepository.save(property);
        return mapToResponse(savedProperty);
    }

    public List<PropertyResponse> getMyProperties(User owner) {
        return propertyRepository.findAllByOwnerId(owner.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PropertyResponse mapToResponse(Property property) {
        return new PropertyResponse(
                property.getId(),
                property.getName(),
                property.getAddress(),
                property.getRentAmount(),
                property.getArea()
        );
    }

    public PropertyResponse updateProperty(Long id, PropertyRequest request, User owner) {
        Property property = propertyRepository.findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new RuntimeException("Property not found access denied"));

        property.setName(request.name());
        property.setAddress(request.address());
        property.setRentAmount(request.rentAmount());
        property.setArea(request.area());

        Property updatedProperty = propertyRepository.save(property);
        return mapToResponse(updatedProperty);
    }

    public void deleteProperty(Long id, User owner) {
        Property property = propertyRepository.findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new RuntimeException("Property not found or acces denied"));
        propertyRepository.delete(property);
    }

}

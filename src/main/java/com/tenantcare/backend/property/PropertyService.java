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
                .adress(request.adress())
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
                property.getAdress(),
                property.getRentAmount(),
                property.getArea()
        );
    }
}

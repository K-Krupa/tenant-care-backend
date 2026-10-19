package com.tenantcare.backend.tenant;

import com.tenantcare.backend.property.Property;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table (name = "tenants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    String phoneNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", referencedColumnName = "id")
    private Property property;
}

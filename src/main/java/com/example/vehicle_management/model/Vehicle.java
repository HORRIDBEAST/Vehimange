package com.example.vehicle_management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Vehicle name is required")
    @Size(min = 2, max = 100, message = "Vehicle name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Fuel type is required")
    @Column(name = "fuel_type", nullable = false)
    private String fuelType;

    @NotBlank(message = "Registration number is required")
    @Column(name = "registration_no", nullable = false, unique = true)
    private String registrationNo;

    @NotBlank(message = "Owner name is required")
    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @NotBlank(message = "Owner address is required")
    @Column(name = "owner_address", nullable = false)
    private String ownerAddress;

    @NotBlank(message = "City is required")
    @Column(nullable = false)
    private String city;

    @NotBlank(message = "State is required")
    @Column(nullable = false)
    private String state;

}

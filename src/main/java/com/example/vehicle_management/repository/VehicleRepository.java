package com.example.vehicle_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.vehicle_management.model.Vehicle;

@Repository // Indicates that this is a Spring Data repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> { // Extends JpaRepository for CRUD operations and more

    // Custom query method to find a vehicle by its registration number.
    // Spring Data JPA automatically creates the implementation based on the method name.
    Optional<Vehicle> findByRegistrationNo(String registrationNo);

    // Custom query method to find vehicles by fuel type.
    List<Vehicle> findByFuelTypeIgnoreCase(String fuelType);

    // Custom query method to find vehicles by city.
    List<Vehicle> findByCityIgnoreCase(String city);

    // Custom query method to find vehicles by state.
    List<Vehicle> findByStateIgnoreCase(String state);

    // Example of a more complex query if needed for multiple search parameters (alternative to service-layer logic)
    // @Query("SELECT v FROM Vehicle v WHERE " +
    //        "(:fuelType IS NULL OR lower(v.fuelType) = lower(:fuelType)) AND " +
    //        "(:city IS NULL OR lower(v.city) = lower(:city)) AND " +
    //        "(:state IS NULL OR lower(v.state) = lower(:state))")
    // List<Vehicle> searchVehicles(@Param("fuelType") String fuelType,
    //                              @Param("city") String city,
    //                              @Param("state") String state);
}

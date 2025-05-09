package com.example.vehicle_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.vehicle_management.model.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByRegistrationNo(String registrationNo);

    List<Vehicle> findByFuelTypeIgnoreCase(String fuelType);

    List<Vehicle> findByCityIgnoreCase(String city);

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

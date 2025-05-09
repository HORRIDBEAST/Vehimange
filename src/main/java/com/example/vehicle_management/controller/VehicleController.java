package com.example.vehicle_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping; // Added for PATCH
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.vehicle_management.exception.ResourceNotFoundException;
import com.example.vehicle_management.model.Vehicle;
import com.example.vehicle_management.service.VehicleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/add/vehicle")
    public ResponseEntity<Vehicle> addVehicle(@Valid @RequestBody Vehicle vehicle) {
        Vehicle newVehicle = vehicleService.addVehicle(vehicle);
        return new ResponseEntity<>(newVehicle, HttpStatus.CREATED);
    }

    @GetMapping("/vehicles")
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/vehicle/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.getVehicleById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", id));
        return ResponseEntity.ok(vehicle);
    }

    /**
     * Endpoint to fully update an existing vehicle (PUT).
     * Expects a complete vehicle object where all required fields are present and valid.
     * URL: PUT /api/vehicle/{id}
     * @param id The ID of the vehicle to update.
     * @param vehicleDetails The complete and validated vehicle data from the request body.
     * @return ResponseEntity with the updated vehicle data and HTTP status 200 (OK).
     */
    @PutMapping("/vehicle/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @Valid @RequestBody Vehicle vehicleDetails) {
        Vehicle updatedVehicle = vehicleService.updateVehicle(id, vehicleDetails);
        return ResponseEntity.ok(updatedVehicle);
    }

    /**
     * Endpoint to partially update an existing vehicle (PATCH).
     * Only the fields provided in the request body will be updated.
     * URL: PATCH /api/vehicle/{id}
     * @param id The ID of the vehicle to update.
     * @param vehiclePatchDetails The vehicle data containing only the fields to be updated.
     * @return ResponseEntity with the updated vehicle data and HTTP status 200 (OK).
     */
    @PatchMapping("/vehicle/{id}")
    public ResponseEntity<Vehicle> patchVehicle(@PathVariable Long id, @RequestBody Vehicle vehiclePatchDetails) {
        Vehicle patchedVehicle = vehicleService.patchVehicle(id, vehiclePatchDetails);
        return ResponseEntity.ok(patchedVehicle);
    }

    @DeleteMapping("/vehicle/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vehicles/search")
    public ResponseEntity<List<Vehicle>> searchVehicles(
            @RequestParam(required = false) String fuel,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state) {
        List<Vehicle> vehicles = vehicleService.searchVehicles(fuel, city, state);
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/vehicle/{registrationNo}/challans")
    public ResponseEntity<?> getVehicleChallans(@PathVariable String registrationNo) {
        List<Object> challans = vehicleService.getChallansByRegistrationNo(registrationNo);
        return ResponseEntity.ok(challans);
    }

    @GetMapping("/vehicle/{registrationNo}/insurance-expiry")
    public ResponseEntity<?> getVehicleInsuranceExpiry(@PathVariable String registrationNo) {
        Object insuranceInfo = vehicleService.getInsuranceExpiryByRegistrationNo(registrationNo);
        return ResponseEntity.ok(insuranceInfo);
    }
}
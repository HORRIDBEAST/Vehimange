package com.example.vehicle_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // For validating request body
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

@RestController // Combination of @Controller and @ResponseBody. Marks this class as a request handler.
@RequestMapping("/api") // Base path for all endpoints in this controller
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired // Dependency injection of VehicleService
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    /**
     * Endpoint to add a new vehicle.
     * URL: POST /api/add/vehicle
     * @param vehicle The vehicle data from the request body. Validated using @Valid.
     * @return ResponseEntity with the created vehicle and HTTP status 201 (Created).
     */
    @PostMapping("/add/vehicle")
    public ResponseEntity<Vehicle> addVehicle(@Valid @RequestBody Vehicle vehicle) {
        Vehicle newVehicle = vehicleService.addVehicle(vehicle);
        return new ResponseEntity<>(newVehicle, HttpStatus.CREATED);
    }

    /**
     * Endpoint to get all vehicles.
     * URL: GET /api/vehicles
     * @return ResponseEntity with a list of all vehicles and HTTP status 200 (OK).
     */
    @GetMapping("/vehicles")
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles); // Shortcut for new ResponseEntity<>(vehicles, HttpStatus.OK)
    }

    /**
     * Endpoint to get a vehicle by its ID.
     * URL: GET /api/vehicle/{id}
     * @param id The ID of the vehicle, passed as a path variable.
     * @return ResponseEntity with the vehicle data and HTTP status 200 (OK),
     * or HTTP status 404 (Not Found) if the vehicle doesn't exist.
     */
    @GetMapping("/vehicle/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.getVehicleById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", id));
        return ResponseEntity.ok(vehicle);
    }

    /**
     * Endpoint to update an existing vehicle.
     * URL: PUT /api/vehicle/{id}
     * @param id The ID of the vehicle to update.
     * @param vehicleDetails The updated vehicle data from the request body.
     * @return ResponseEntity with the updated vehicle data and HTTP status 200 (OK),
     * or HTTP status 404 (Not Found) if the vehicle doesn't exist.
     */
    @PutMapping("/vehicle/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @Valid @RequestBody Vehicle vehicleDetails) {
        // @Valid here ensures that if the client sends a full vehicle object for update, it's still validated.
        // The service layer handles partial updates.
        Vehicle updatedVehicle = vehicleService.updateVehicle(id, vehicleDetails);
        return ResponseEntity.ok(updatedVehicle);
    }

    /**
     * Endpoint to delete a vehicle by its ID.
     * URL: DELETE /api/vehicle/{id}
     * @param id The ID of the vehicle to delete.
     * @return ResponseEntity with HTTP status 204 (No Content) on successful deletion,
     * or HTTP status 404 (Not Found) if the vehicle doesn't exist.
     */
    @DeleteMapping("/vehicle/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    /**
     * Endpoint to search for vehicles by fuel type, city, or state.
     * Examples:
     * GET /api/vehicles/search?fuel=Petrol
     * GET /api/vehicles/search?city=Patna
     * GET /api/vehicles/search?state=Bihar
     * @param fuelType Optional request parameter for fuel type.
     * @param city Optional request parameter for city.
     * @param state Optional request parameter for state.
     * @return ResponseEntity with a list of matching vehicles and HTTP status 200 (OK).
     */
    @GetMapping("/vehicles/search")
    public ResponseEntity<List<Vehicle>> searchVehicles(
            @RequestParam(required = false) String fuel,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state) {
        List<Vehicle> vehicles = vehicleService.searchVehicles(fuel, city, state);
        return ResponseEntity.ok(vehicles);
    }


    // --- Brownie Points Endpoints (Conceptual) ---

    /**
     * Placeholder Endpoint: Get challans for a vehicle by registration number.
     * URL: GET /api/vehicle/{registrationNo}/challans
     * @param registrationNo The vehicle's registration number.
     * @return ResponseEntity with challan data (conceptual).
     */
    @GetMapping("/vehicle/{registrationNo}/challans")
    public ResponseEntity<?> getVehicleChallans(@PathVariable String registrationNo) {
        // This would call the service method that interacts with an external API
        List<Object> challans = vehicleService.getChallansByRegistrationNo(registrationNo);
        if (challans.isEmpty() || (challans.size() == 1 && challans.get(0).toString().startsWith("Placeholder:"))) {
             // You might want a more specific response if it's just a placeholder or truly no challans
            return ResponseEntity.ok(challans); // Or perhaps a custom message
        }
        return ResponseEntity.ok(challans);
    }

    /**
     * Placeholder Endpoint: Get insurance expiry date for a vehicle by registration number.
     * URL: GET /api/vehicle/{registrationNo}/insurance-expiry
     * @param registrationNo The vehicle's registration number.
     * @return ResponseEntity with insurance expiry data (conceptual).
     */
    @GetMapping("/vehicle/{registrationNo}/insurance-expiry")
    public ResponseEntity<?> getVehicleInsuranceExpiry(@PathVariable String registrationNo) {
        // This would call the service method that interacts with an external API
        Object insuranceInfo = vehicleService.getInsuranceExpiryByRegistrationNo(registrationNo);
         if (insuranceInfo == null || insuranceInfo.toString().startsWith("Placeholder:")) {
            // You might want a more specific response
            return ResponseEntity.ok(insuranceInfo); // Or perhaps a custom message or 404 if truly not found
        }
        return ResponseEntity.ok(insuranceInfo);
    }
}

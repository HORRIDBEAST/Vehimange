package com.example.vehicle_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.vehicle_management.exception.DuplicateRegistrationException;
import com.example.vehicle_management.exception.ResourceNotFoundException; // For checking if string is null or empty
import com.example.vehicle_management.model.Vehicle;
import com.example.vehicle_management.repository.VehicleRepository;

@Service // Marks this class as a Spring service component
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired // Dependency injection of VehicleRepository
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Adds a new vehicle to the database.
     * Checks for duplicate registration number before saving.
     * @param vehicle The vehicle object to be added.
     * @return The saved vehicle object with its generated ID.
     * @throws DuplicateRegistrationException if registration number already exists.
     */
    @Transactional // Ensures this operation is atomic
    public Vehicle addVehicle(Vehicle vehicle) {
        // Check if a vehicle with the same registration number already exists
        Optional<Vehicle> existingVehicle = vehicleRepository.findByRegistrationNo(vehicle.getRegistrationNo());
        if (existingVehicle.isPresent()) {
            throw new DuplicateRegistrationException("Vehicle with registration number '" + vehicle.getRegistrationNo() + "' already exists.");
        }
        return vehicleRepository.save(vehicle);
    }

    /**
     * Retrieves all vehicles from the database.
     * @return A list of all vehicles.
     */
    @Transactional(readOnly = true) // Optimizes for read operations
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    /**
     * Retrieves a vehicle by its ID.
     * @param id The ID of the vehicle to retrieve.
     * @return An Optional containing the vehicle if found, or an empty Optional otherwise.
     */
    @Transactional(readOnly = true)
    public Optional<Vehicle> getVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }

    /**
     * Updates an existing vehicle's details.
     * @param id The ID of the vehicle to update.
     * @param vehicleDetails The new details for the vehicle.
     * @return The updated vehicle object.
     * @throws ResourceNotFoundException if the vehicle with the given ID is not found.
     * @throws DuplicateRegistrationException if trying to update to an existing registration number (owned by another vehicle).
     */
    @Transactional
    public Vehicle updateVehicle(Long id, Vehicle vehicleDetails) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", id));

        // Check for duplicate registration number if it's being changed
        if (vehicleDetails.getRegistrationNo() != null && !vehicleDetails.getRegistrationNo().equalsIgnoreCase(vehicle.getRegistrationNo())) {
            Optional<Vehicle> existingVehicleWithNewRegNo = vehicleRepository.findByRegistrationNo(vehicleDetails.getRegistrationNo());
            if (existingVehicleWithNewRegNo.isPresent()) {
                throw new DuplicateRegistrationException("Registration number '" + vehicleDetails.getRegistrationNo() + "' is already in use by another vehicle.");
            }
            vehicle.setRegistrationNo(vehicleDetails.getRegistrationNo());
        }

        // Update fields if they are provided in the request
        if (StringUtils.hasText(vehicleDetails.getName())) {
            vehicle.setName(vehicleDetails.getName());
        }
        if (StringUtils.hasText(vehicleDetails.getFuelType())) {
            vehicle.setFuelType(vehicleDetails.getFuelType());
        }
        if (StringUtils.hasText(vehicleDetails.getOwnerName())) {
            vehicle.setOwnerName(vehicleDetails.getOwnerName());
        }
        if (StringUtils.hasText(vehicleDetails.getOwnerAddress())) {
            vehicle.setOwnerAddress(vehicleDetails.getOwnerAddress());
        }
        if (StringUtils.hasText(vehicleDetails.getCity())) {
            vehicle.setCity(vehicleDetails.getCity());
        }
        if (StringUtils.hasText(vehicleDetails.getState())) {
            vehicle.setState(vehicleDetails.getState());
        }

        return vehicleRepository.save(vehicle);
    }

    /**
     * Deletes a vehicle by its ID.
     * @param id The ID of the vehicle to delete.
     * @throws ResourceNotFoundException if the vehicle with the given ID is not found.
     */
    @Transactional
    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle", "id", id);
        }
        vehicleRepository.deleteById(id);
    }

    /**
     * Searches for vehicles based on fuel type, city, or state.
     * Only one search parameter should be provided at a time for this implementation.
     * @param fuelType Optional fuel type to search by.
     * @param city Optional city to search by.
     * @param state Optional state to search by.
     * @return A list of vehicles matching the search criteria.
     */
    @Transactional(readOnly = true)
    public List<Vehicle> searchVehicles(String fuelType, String city, String state) {
        if (StringUtils.hasText(fuelType)) {
            return vehicleRepository.findByFuelTypeIgnoreCase(fuelType);
        } else if (StringUtils.hasText(city)) {
            return vehicleRepository.findByCityIgnoreCase(city);
        } else if (StringUtils.hasText(state)) {
            return vehicleRepository.findByStateIgnoreCase(state);
        }
        return getAllVehicles(); // Return all if no specific search criteria is provided
    }

    // --- Placeholder methods for Brownie Points ---

    /**
     * Placeholder: Searches for challans based on vehicle registration number.
     * Actual implementation would require integrating with an external challan API.
     * @param registrationNo The vehicle's registration plate number.
     * @return A list of challan details (conceptual).
     */
    public List<Object> getChallansByRegistrationNo(String registrationNo) {
        // In a real application, you would call an external API here.
        // Example:
        // String apiUrl = "https://api.example.com/challans?registrationNo=" + registrationNo;
        // ResponseEntity<List<ChallanDto>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<ChallanDto>>() {});
        // return response.getBody();
        System.out.println("Placeholder: Searching challans for registration number: " + registrationNo);
        // For now, returning an empty list or mock data.
        return List.of("Challan data for " + registrationNo + " would appear here (from external API).");
    }

    /**
     * Placeholder: Gets the insurance expiry date for a vehicle.
     * Actual implementation would require integrating with an external insurance API.
     * @param registrationNo The vehicle's registration plate number.
     * @return Insurance expiry details (conceptual).
     */
    public Object getInsuranceExpiryByRegistrationNo(String registrationNo) {
        // In a real application, you would call an external API here.
        System.out.println("Placeholder: Fetching insurance expiry for registration number: " + registrationNo);
        // For now, returning a placeholder string or mock data.
        return "Insurance expiry data for " + registrationNo + " would appear here (from external API).";
    }
}

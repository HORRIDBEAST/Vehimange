package com.example.vehicle_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.vehicle_management.exception.DuplicateRegistrationException;
import com.example.vehicle_management.exception.ResourceNotFoundException;
import com.example.vehicle_management.model.Vehicle;
import com.example.vehicle_management.repository.VehicleRepository;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public Vehicle addVehicle(Vehicle vehicle) {
        Optional<Vehicle> existingVehicle = vehicleRepository.findByRegistrationNo(vehicle.getRegistrationNo());
        if (existingVehicle.isPresent()) {
            throw new DuplicateRegistrationException("Vehicle with registration number '" + vehicle.getRegistrationNo() + "' already exists.");
        }
        return vehicleRepository.save(vehicle);
    }

    @Transactional(readOnly = true)
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Vehicle> getVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }

    /**
     * Updates an existing vehicle's details (Full Update - for PUT).
     * Expects vehicleDetails to be a complete representation with all required fields validated by the controller.
     * @param id The ID of the vehicle to update.
     * @param vehicleDetails The new complete details for the vehicle.
     * @return The updated vehicle object.
     * @throws ResourceNotFoundException if the vehicle with the given ID is not found.
     * @throws DuplicateRegistrationException if trying to update to an existing registration number (owned by another vehicle).
     */
    @Transactional
    public Vehicle updateVehicle(Long id, Vehicle vehicleDetails) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", id));

        if (!vehicleDetails.getRegistrationNo().equalsIgnoreCase(vehicle.getRegistrationNo())) {
            Optional<Vehicle> existingVehicleWithNewRegNo = vehicleRepository.findByRegistrationNo(vehicleDetails.getRegistrationNo());
            if (existingVehicleWithNewRegNo.isPresent()) {
                throw new DuplicateRegistrationException("Registration number '" + vehicleDetails.getRegistrationNo() + "' is already in use by another vehicle.");
            }
        }

        vehicle.setName(vehicleDetails.getName());
        vehicle.setFuelType(vehicleDetails.getFuelType());
        vehicle.setRegistrationNo(vehicleDetails.getRegistrationNo());
        vehicle.setOwnerName(vehicleDetails.getOwnerName());
        vehicle.setOwnerAddress(vehicleDetails.getOwnerAddress());
        vehicle.setCity(vehicleDetails.getCity());
        vehicle.setState(vehicleDetails.getState());

        return vehicleRepository.save(vehicle);
    }

    /**
     * Partially updates an existing vehicle's details (Partial Update - for PATCH).
     * Only updates fields that are provided (non-null and non-empty) in vehiclePatchDetails.
     * @param id The ID of the vehicle to update.
     * @param vehiclePatchDetails The vehicle details to patch. Fields not provided or empty will not be changed.
     * @return The updated vehicle object.
     * @throws ResourceNotFoundException if the vehicle with the given ID is not found.
     * @throws DuplicateRegistrationException if trying to update to an existing registration number (owned by another vehicle).
     */
    @Transactional
    public Vehicle patchVehicle(Long id, Vehicle vehiclePatchDetails) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", id));

        boolean hasChanges = false;

        if (StringUtils.hasText(vehiclePatchDetails.getName())) {
            vehicle.setName(vehiclePatchDetails.getName());
            hasChanges = true;
        }
        if (StringUtils.hasText(vehiclePatchDetails.getFuelType())) {
            vehicle.setFuelType(vehiclePatchDetails.getFuelType());
            hasChanges = true;
        }
        if (StringUtils.hasText(vehiclePatchDetails.getRegistrationNo()) &&
            !vehiclePatchDetails.getRegistrationNo().equalsIgnoreCase(vehicle.getRegistrationNo())) {
            Optional<Vehicle> existingVehicleWithNewRegNo = vehicleRepository.findByRegistrationNo(vehiclePatchDetails.getRegistrationNo());
            if (existingVehicleWithNewRegNo.isPresent()) {
                throw new DuplicateRegistrationException("Registration number '" + vehiclePatchDetails.getRegistrationNo() + "' is already in use by another vehicle.");
            }
            vehicle.setRegistrationNo(vehiclePatchDetails.getRegistrationNo());
            hasChanges = true;
        }
        if (StringUtils.hasText(vehiclePatchDetails.getOwnerName())) {
            vehicle.setOwnerName(vehiclePatchDetails.getOwnerName());
            hasChanges = true;
        }
        if (StringUtils.hasText(vehiclePatchDetails.getOwnerAddress())) {
            vehicle.setOwnerAddress(vehiclePatchDetails.getOwnerAddress());
            hasChanges = true;
        }
        if (StringUtils.hasText(vehiclePatchDetails.getCity())) {
            vehicle.setCity(vehiclePatchDetails.getCity());
            hasChanges = true;
        }
        if (StringUtils.hasText(vehiclePatchDetails.getState())) {
            vehicle.setState(vehiclePatchDetails.getState());
            hasChanges = true;
        }

        if (hasChanges) {
            return vehicleRepository.save(vehicle);
        }
        return vehicle;
    }

    @Transactional
    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle", "id", id);
        }
        vehicleRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Vehicle> searchVehicles(String fuelType, String city, String state) {
        if (StringUtils.hasText(fuelType)) {
            return vehicleRepository.findByFuelTypeIgnoreCase(fuelType);
        } else if (StringUtils.hasText(city)) {
            return vehicleRepository.findByCityIgnoreCase(city);
        } else if (StringUtils.hasText(state)) {
            return vehicleRepository.findByStateIgnoreCase(state);
        }
        return getAllVehicles();
    }

    public List<Object> getChallansByRegistrationNo(String registrationNo) {
        System.out.println("Placeholder: Searching challans for registration number: " + registrationNo);
        return List.of("Challan data for " + registrationNo + " would appear here (from external API).");
    }

    public Object getInsuranceExpiryByRegistrationNo(String registrationNo) {
        System.out.println("Placeholder: Fetching insurance expiry for registration number: " + registrationNo);
        return "Insurance expiry data for " + registrationNo + " would appear here (from external API).";
    }
}
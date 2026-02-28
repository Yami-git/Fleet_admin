package com.logistics.fleet_backend.controller;

import com.logistics.fleet_backend.model.Location;
import com.logistics.fleet_backend.model.Vehicle;
import com.logistics.fleet_backend.repository.LocationRepository;
import com.logistics.fleet_backend.repository.VehicleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
    
    private final LocationRepository locationRepository;
    private final VehicleRepository vehicleRepository;

    public LocationController(LocationRepository locationRepository, VehicleRepository vehicleRepository) {
        this.locationRepository = locationRepository;
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Get all trucks within X meters of a location
     */

    @GetMapping
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @PostMapping
    public Location addLocation(@RequestBody Location location) {
        return locationRepository.save(location);
    }

    /**
     * Find all vehicles within radius of a location
     * Usage: GET /api/locations/{id}/vehicles-nearby?radius=5000 (5km)
     */
    @GetMapping("/{locationId}/vehicles-nearby")
    public List<Vehicle> getVehiclesNearLocation(
            @PathVariable String locationId,
            @RequestParam(defaultValue = "5000") double radius) {
        
        Location location = locationRepository.findById(locationId).orElse(null);
        if (location == null) {
            return List.of();
        }
        return vehicleRepository.findWithinRadius(
            location.getLatitude(), 
            location.getLongitude(), 
            radius
        );
    }

    /**
     * Find nearest vehicle to a location
     */
    @GetMapping("/{id}/nearest-vehicle")
    public Vehicle getNearestVehicle(@PathVariable String id) {
        Location location = locationRepository.findById(id).orElse(null);
        if (location == null) {
            return null;
        }
        return vehicleRepository.findNearestVehicle(
            location.getLatitude(), 
            location.getLongitude()
        );
    }
}

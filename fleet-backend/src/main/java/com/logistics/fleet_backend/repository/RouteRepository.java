package com.logistics.fleet_backend.repository;

import com.logistics.fleet_backend.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, String> {
    
    // Find active route for a specific truck
    Optional<Route> findByTruckIdAndStatus(String truckId, String status);
    
    // Find all routes for a truck
    List<Route> findByTruckId(String truckId);
    
    // Find routes by status
    List<Route> findByStatus(String status);
}

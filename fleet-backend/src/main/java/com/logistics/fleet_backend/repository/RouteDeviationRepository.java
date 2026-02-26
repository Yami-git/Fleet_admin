package com.logistics.fleet_backend.repository;

import com.logistics.fleet_backend.model.RouteDeviation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RouteDeviationRepository extends JpaRepository<RouteDeviation, String> {
    
    // Get all deviations for a specific truck
    List<RouteDeviation> findByTruckId(String truckId);
    
    // Get all deviations for a specific route
    List<RouteDeviation> findByRouteId(String routeId);
    
    // Get active (unresolved) deviations
    List<RouteDeviation> findByStatus(String status);
    
    // Get deviations by severity
    List<RouteDeviation> findBySeverity(String severity);
}

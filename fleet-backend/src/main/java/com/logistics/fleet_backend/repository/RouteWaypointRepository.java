package com.logistics.fleet_backend.repository;

import com.logistics.fleet_backend.model.RouteWaypoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RouteWaypointRepository extends JpaRepository<RouteWaypoint, Long> {
    
    // Get all waypoints for a route, sorted by sequence
    List<RouteWaypoint> findByRouteIdOrderBySequenceNumber(String routeId);
    
    // Get waypoints by route ID
    List<RouteWaypoint> findByRouteId(String routeId);
}

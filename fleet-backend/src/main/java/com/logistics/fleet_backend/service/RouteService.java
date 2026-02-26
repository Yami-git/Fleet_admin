package com.logistics.fleet_backend.service;

import com.logistics.fleet_backend.model.Route;
import com.logistics.fleet_backend.model.RouteWaypoint;
import com.logistics.fleet_backend.repository.RouteRepository;
import com.logistics.fleet_backend.repository.RouteWaypointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RouteService {

    private final RouteRepository routeRepository;
    private final RouteWaypointRepository waypointRepository;

    public RouteService(RouteRepository routeRepository, RouteWaypointRepository waypointRepository) {
        this.routeRepository = routeRepository;
        this.waypointRepository = waypointRepository;
    }

    /**
     * Create a new route with waypoints
     */
    @Transactional
    public Route createRoute(String truckId, String startLocationId, String endLocationId, 
                             List<RouteWaypoint> waypoints) {
        String routeId = "ROUTE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        Route route = new Route();
        route.setRouteId(routeId);
        route.setTruckId(truckId);
        route.setStartLocationId(startLocationId);
        route.setEndLocationId(endLocationId);
        route.setStatus("PLANNED");
        route.setPlannedDeparture(LocalDateTime.now());
        
        // Save waypoints with route ID
        for (RouteWaypoint wp : waypoints) {
            wp.setRouteId(routeId);
        }
        waypointRepository.saveAll(waypoints);
        
        return routeRepository.save(route);
    }

    /**
     * Activate a route (start the delivery)
     */
    @Transactional
    public Route activateRoute(String routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found: " + routeId));
        
        route.setStatus("ACTIVE");
        route.setPlannedDeparture(LocalDateTime.now());
        return routeRepository.save(route);
    }

    /**
     * Complete a route
     */
    @Transactional
    public Route completeRoute(String routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found: " + routeId));
        
        route.setStatus("COMPLETED");
        route.setPlannedArrival(LocalDateTime.now());
        return routeRepository.save(route);
    }

    /**
     * Get active route for a truck
     */
    public Route getActiveRoute(String truckId) {
        return routeRepository.findByTruckIdAndStatus(truckId, "ACTIVE").orElse(null);
    }

    /**
     * Get all waypoints for a route
     */
    public List<RouteWaypoint> getWaypoints(String routeId) {
        return waypointRepository.findByRouteIdOrderBySequenceNumber(routeId);
    }

    /**
     * Get all routes
     */
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    /**
     * Get route by ID
     */
    public Route getRouteById(String routeId) {
        return routeRepository.findById(routeId).orElse(null);
    }
}

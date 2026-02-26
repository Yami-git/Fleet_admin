package com.logistics.fleet_backend.controller;

import com.logistics.fleet_backend.model.Route;
import com.logistics.fleet_backend.model.RouteWaypoint;
import com.logistics.fleet_backend.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    /**
     * Get all routes
     */
    @GetMapping
    public List<Route> getAllRoutes() {
        return routeService.getAllRoutes();
    }

    /**
     * Get route by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Route> getRouteById(@PathVariable String id) {
        Route route = routeService.getRouteById(id);
        if (route == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(route);
    }

    /**
     * Get active route for a truck
     */
    @GetMapping("/active/{truckId}")
    public ResponseEntity<Route> getActiveRoute(@PathVariable String truckId) {
        Route route = routeService.getActiveRoute(truckId);
        if (route == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(route);
    }

    /**
     * Get waypoints for a route
     */
    @GetMapping("/{id}/waypoints")
    public ResponseEntity<List<RouteWaypoint>> getWaypoints(@PathVariable String id) {
        List<RouteWaypoint> waypoints = routeService.getWaypoints(id);
        return ResponseEntity.ok(waypoints);
    }

    /**
     * Create a new route
     */
    @PostMapping
    public Route createRoute(@RequestBody Map<String, Object> request) {
        String truckId = (String) request.get("truckId");
        String startLocationId = (String) request.get("startLocationId");
        String endLocationId = (String) request.get("endLocationId");
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> waypointData = (List<Map<String, Object>>) request.get("waypoints");
        
        List<RouteWaypoint> waypoints = waypointData.stream()
                .map(wp -> new RouteWaypoint(
                        null, // routeId will be set in service
                        ((Number) wp.get("sequenceNumber")).intValue(),
                        ((Number) wp.get("latitude")).doubleValue(),
                        ((Number) wp.get("longitude")).doubleValue(),
                        ((Number) wp.get("expectedArrivalTime")).doubleValue()
                ))
                .toList();
        
        return routeService.createRoute(truckId, startLocationId, endLocationId, waypoints);
    }

    /**
     * Activate a route
     */
    @PutMapping("/{id}/activate")
    public ResponseEntity<Route> activateRoute(@PathVariable String id) {
        try {
            Route route = routeService.activateRoute(id);
            return ResponseEntity.ok(route);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Complete a route
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<Route> completeRoute(@PathVariable String id) {
        try {
            Route route = routeService.completeRoute(id);
            return ResponseEntity.ok(route);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

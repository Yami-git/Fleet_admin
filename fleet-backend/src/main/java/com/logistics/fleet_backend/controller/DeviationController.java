package com.logistics.fleet_backend.controller;

import com.logistics.fleet_backend.model.RouteDeviation;
import com.logistics.fleet_backend.service.AnomalyDetectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deviations")
public class DeviationController {

    private final AnomalyDetectionService anomalyDetectionService;

    public DeviationController(AnomalyDetectionService anomalyDetectionService) {
        this.anomalyDetectionService = anomalyDetectionService;
    }

    /**
     * Get all deviations
     */
    @GetMapping
    public List<RouteDeviation> getAllDeviations() {
        return anomalyDetectionService.getAllDeviations();
    }

    /**
     * Get active (new) deviations
     */
    @GetMapping("/active")
    public List<RouteDeviation> getActiveDeviations() {
        return anomalyDetectionService.getActiveDeviations();
    }

    /**
     * Get deviations for a specific truck
     */
    @GetMapping("/truck/{truckId}")
    public List<RouteDeviation> getDeviationsByTruck(@PathVariable String truckId) {
        return anomalyDetectionService.getDeviationsByTruck(truckId);
    }

    /**
     * Acknowledge a deviation
     */
    @PutMapping("/{id}/acknowledge")
    public ResponseEntity<RouteDeviation> acknowledgeDeviation(@PathVariable String id) {
        try {
            RouteDeviation deviation = anomalyDetectionService.acknowledgeDeviation(id);
            return ResponseEntity.ok(deviation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Resolve a deviation
     */
    @PutMapping("/{id}/resolve")
    public ResponseEntity<RouteDeviation> resolveDeviation(@PathVariable String id) {
        try {
            RouteDeviation deviation = anomalyDetectionService.resolveDeviation(id);
            return ResponseEntity.ok(deviation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

package com.logistics.fleet_backend.model;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class RouteDeviation {
    @Id 
    private String deviationId;
    private String routeId;
    private String truckId;
    private double deviationDistance; // meters
    private double currentLat;
    private double currentLon;
    private LocalDateTime detectedAt;
    private String severity; // LOW, MEDIUM, HIGH
    private String status; // NEW, ACKNOWLEDGED, RESOLVED

    public RouteDeviation() {}

    public RouteDeviation(String deviationId, String routeId, String truckId, 
                          double deviationDistance, double currentLat, double currentLon) {
        this.deviationId = deviationId;
        this.routeId = routeId;
        this.truckId = truckId;
        this.deviationDistance = deviationDistance;
        this.currentLat = currentLat;
        this.currentLon = currentLon;
        this.detectedAt = LocalDateTime.now();
        this.status = "NEW";
        this.severity = calculateSeverity(deviationDistance);
    }

    private String calculateSeverity(double distance) {
        if (distance > 1000) return "HIGH";
        if (distance > 500) return "MEDIUM";
        return "LOW";
    }

    // Getters and Setters
    public String getDeviationId() { return deviationId; }
    public void setDeviationId(String deviationId) { this.deviationId = deviationId; }

    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }

    public String getTruckId() { return truckId; }
    public void setTruckId(String truckId) { this.truckId = truckId; }

    public double getDeviationDistance() { return deviationDistance; }
    public void setDeviationDistance(double deviationDistance) { this.deviationDistance = deviationDistance; }

    public double getCurrentLat() { return currentLat; }
    public void setCurrentLat(double currentLat) { this.currentLat = currentLat; }

    public double getCurrentLon() { return currentLon; }
    public void setCurrentLon(double currentLon) { this.currentLon = currentLon; }

    public LocalDateTime getDetectedAt() { return detectedAt; }
    public void setDetectedAt(LocalDateTime detectedAt) { this.detectedAt = detectedAt; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

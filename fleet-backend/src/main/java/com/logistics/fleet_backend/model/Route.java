package com.logistics.fleet_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Route {
    @Id
    private String routeId;
    private String truckId;
    private String startLocationId;
    private String endLocationId;
    private String status; // PLANNED, ACTIVE, COMPLETED
    private LocalDateTime plannedDeparture;
    private LocalDateTime plannedArrival;

    public Route() {}

    public Route(String routeId, String truckId, String startLocationId, String endLocationId) {
        this.routeId = routeId;
        this.truckId = truckId;
        this.startLocationId = startLocationId;
        this.endLocationId = endLocationId;
        this.status = "PLANNED";
    }

    // Getters and Setters
    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }

    public String getTruckId() { return truckId; }
    public void setTruckId(String truckId) { this.truckId = truckId; }

    public String getStartLocationId() { return startLocationId; }
    public void setStartLocationId(String startLocationId) { this.startLocationId = startLocationId; }

    public String getEndLocationId() { return endLocationId; }
    public void setEndLocationId(String endLocationId) { this.endLocationId = endLocationId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getPlannedDeparture() { return plannedDeparture; }
    public void setPlannedDeparture(LocalDateTime plannedDeparture) { this.plannedDeparture = plannedDeparture; }

    public LocalDateTime getPlannedArrival() { return plannedArrival; }
    public void setPlannedArrival(LocalDateTime plannedArrival) { this.plannedArrival = plannedArrival; }
}

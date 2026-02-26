package com.logistics.fleet_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class RouteWaypoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String routeId;
    private int sequenceNumber;
    private double latitude;
    private double longitude;
    private double expectedArrivalTime; // minutes from start

    public RouteWaypoint() {}

    public RouteWaypoint(String routeId, int sequenceNumber, double latitude, double longitude, double expectedArrivalTime) {
        this.routeId = routeId;
        this.sequenceNumber = sequenceNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.expectedArrivalTime = expectedArrivalTime;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }

    public int getSequenceNumber() { return sequenceNumber; }
    public void setSequenceNumber(int sequenceNumber) { this.sequenceNumber = sequenceNumber; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getExpectedArrivalTime() { return expectedArrivalTime; }
    public void setExpectedArrivalTime(double expectedArrivalTime) { this.expectedArrivalTime = expectedArrivalTime; }
}

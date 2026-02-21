package com.logistics.fleet_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Vehicle {
    @Id
    private String truckId;
    private double latitude;
    private double longitude;

    public Vehicle(){}
    public Vehicle(String truckId, double latitude, double longitude){
        this.truckId=truckId;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public String getTruckId() {
        return truckId;
    }

    public void setTruckId(String truckId) {
        this.truckId = truckId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

package com.logistics.fleet_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.locationtech.jts.geom.Point;

@Entity
public class Location {
    @Id
    private String locationId;
    private String name;
    private String type;
    private double latitude;
    private double longitude;
    private Point location;

    public Location(){}

    public Location(String locationId, String name, String type, double latitude, double longitude){
        this.locationId = locationId;
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocationId() { return locationId; }
    public void setLocationId(String locationId) { this.locationId = locationId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public Point getLocation() { return location; }
    public void setLocation(Point location) { this.location = location; }
}

# Tier 5: Route Anomaly Detection - Implementation Guide

## Overview
Detect when delivery trucks deviate from their planned routes in real-time, alerting operators immediately to potential delays or issues.

---

## 1. Data Models

### 1.1 Route Entity
```
java
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
    // List of waypoints (coordinates forming the planned path)
}
```

### 1.2 RouteWaypoint Entity
```
java
@Entity
public class RouteWaypoint {
    @Id
    private Long id;
    private String routeId;
    private int sequenceNumber;
    private double latitude;
    private double longitude;
    private double expectedArrivalTime; // minutes from start
}
```

### 1.3 RouteDeviation Alert Entity
```
java
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
}
```

---

## 2. Core Algorithms

### 2.1 Haversine Distance (for coordinate distance)
```
java
public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
    final int R = 6371000; // Earth's radius in meters
    double latRad1 = Math.toRadians(lat1);
    double latRad2 = Math.toRadians(lat2);
    double deltaLat = Math.toRadians(lat2 - lat1);
    double deltaLon = Math.toRadians(lon2 - lon1);
    
    double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
               Math.cos(latRad1) * Math.cos(latRad2) *
               Math.sin(deltaLon/2) * Math.sin(deltaLon/2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    return R * c;
}
```

### 2.2 Point-to-Line Distance (for route deviation)
```
java
public static double pointToLineDistance(double px, double py, 
                                          double x1, double y1, 
                                          double x2, double y2) {
    // Vector from line start to end
    double dx = x2 - x1;
    double dy = y2 - y1;
    
    // If line is a point
    if (dx == 0 && dy == 0) return haversineDistance(px, py, x1, y1);
    
    // Parameter t represents position on line (-infinity to +infinity)
    double t = ((px - x1) * dx + (py - y1) * dy) / (dx * dx + dy * dy);
    
    // Clamp to line segment
    t = Math.max(0, Math.min(1, t));
    
    // Closest point on line
    double closestX = x1 + t * dx;
    double closestY = y1 + t * dy;
    
    return haversineDistance(px, py, closestX, closestY);
}
```

### 2.3 Deviation Detection Logic
```
java
public RouteDeviation checkDeviation(String truckId, double currentLat, double currentLon) {
    // 1. Get active route for truck
    Route route = routeRepository.findActiveRoute(truckId);
    if (route == null) return null;
    
    // 2. Get all waypoints sorted by sequence
    List<RouteWaypoint> waypoints = routeWaypointRepository.findByRouteIdOrderBySequence(route.getRouteId());
    
    // 3. Find closest segment on route
    double minDistance = Double.MAX_VALUE;
    int closestSegmentIndex = 0;
    
    for (int i = 0; i < waypoints.size() - 1; i++) {
        double dist = pointToLineDistance(
            currentLat, currentLon,
            waypoints.get(i).getLatitude(), waypoints.get(i).getLongitude(),
            waypoints.get(i+1).getLatitude(), waypoints.get(i+1).getLongitude()
        );
        if (dist < minDistance) {
            minDistance = dist;
            closestSegmentIndex = i;
        }
    }
    
    // 4. Check if deviation exceeds threshold
    double DEVIATION_THRESHOLD_METERS = 500; // 500m
    if (minDistance > DEVIATION_THRESHOLD_METERS) {
        return createDeviationAlert(truckId, route, minDistance, currentLat, currentLon);
    }
    
    return null;
}
```

---

## 3. REST API Endpoints

### Route Management
```
POST   /api/routes                 - Create new route
GET    /api/routes                  - Get all routes
GET    /api/routes/{id}            - Get route details
PUT    /api/routes/{id}/activate   - Activate route for truck
PUT    /api/routes/{id}/complete   - Mark route as completed
```

### Deviation Alerts
```
GET    /api/deviations                    - Get all deviations
GET    /api/deviations/truck/{truckId}    - Get deviations for truck
PUT    /api/deviations/{id}/acknowledge   - Acknowledge deviation
PUT    /api/deviations/{id}/resolve       - Mark as resolved
GET    /api/deviations/active             - Get active deviations
```

---

## 4. Implementation Steps

### Step 1: Create Entities
- [ ] Route.java
- [ ] RouteWaypoint.java  
- [ ] RouteDeviation.java

### Step 2: Create Repositories
- [ ] RouteRepository
- [ ] RouteWaypointRepository
- [ ] RouteDeviationRepository

### Step 3: Create Services
- [ ] RouteService - manage routes
- [ ] AnomalyDetectionService - core detection logic

### Step 4: Create Controllers
- [ ] RouteController
- [ ] DeviationController

### Step 5: Integrate with WebSocket
- [ ] Publish deviations to /topic/deviations

### Step 6: Add Sample Routes
- [ ] Seed sample routes with waypoints

---

## 5. Key Configuration

```
properties
# Deviation detection settings
anomaly.deviation.threshold.meters=500
anomaly.deviation.check.interval.seconds=30
anomaly.severity.high.threshold.meters=1000
anomaly.severity.medium.threshold.meters=500
anomaly.severity.low.threshold.meters=200
```

---

## 6. Resume Talking Points

After implementing, you can say:
> "Implemented a real-time route anomaly detection system using geometric algorithms (point-to-line distance) to identify when delivery trucks deviate from planned routes. The system processes 1,000+ vehicle streams concurrently and triggers immediate alerts when deviation exceeds configurable thresholds."

---

## 7. Files to Create

1. `model/Route.java`
2. `model/RouteWaypoint.java`
3. `model/RouteDeviation.java`
4. `repository/RouteRepository.java`
5. `repository/RouteWaypointRepository.java`
6. `repository/RouteDeviationRepository.java`
7. `service/RouteService.java`
8. `service/AnomalyDetectionService.java`
9. `controller/RouteController.java`
10. `controller/DeviationController.java`
11. `config/AnomalyConfig.java`

---

This skeleton provides the complete roadmap for implementing Tier 5. Each component is modular and can be developed independently.

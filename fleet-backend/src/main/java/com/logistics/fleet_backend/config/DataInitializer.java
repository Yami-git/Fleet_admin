package com.logistics.fleet_backend.config;

import com.logistics.fleet_backend.model.Location;
import com.logistics.fleet_backend.model.Route;
import com.logistics.fleet_backend.model.RouteWaypoint;
import com.logistics.fleet_backend.repository.LocationRepository;
import com.logistics.fleet_backend.repository.RouteRepository;
import com.logistics.fleet_backend.repository.RouteWaypointRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            LocationRepository locationRepository,
            RouteRepository routeRepository,
            RouteWaypointRepository waypointRepository) {
        return args -> {
            // Initialize warehouse locations
            if (locationRepository.count() == 0) {
                System.out.println("📦 Initializing sample warehouse locations...");
                
                List<Location> warehouses = Arrays.asList(
                    // Cape Town Area Warehouses
                    new Location("WH-001", "Cape Town Central Warehouse", "WAREHOUSE", -33.9249, 18.4241),
                    new Location("WH-002", "Stellenbosch Depot", "DEPOT", -33.9328, 18.8601),
                    new Location("WH-003", "Paarl Distribution Center", "WAREHOUSE", -33.7167, 18.9667),
                    new Location("WH-004", "George Logistics Hub", "WAREHOUSE", -33.9618, 22.4577),
                    new Location("WH-005", "Bellville Depot", "DEPOT", -33.9000, 18.6333),
                    new Location("WH-006", "Durban North Warehouse", "WAREHOUSE", -29.8000, 31.0333),
                    new Location("WH-007", "Johannesburg Central", "WAREHOUSE", -26.2041, 28.0473),
                    new Location("WH-008", "Pretoria Depot", "DEPOT", -25.7479, 28.2293),
                    new Location("WH-009", "Port Elizabeth Hub", "WAREHOUSE", -33.9608, 25.6022),
                    new Location("WH-010", "Bloemfontein Warehouse", "WAREHOUSE", -29.0852, 26.1596)
                );

                locationRepository.saveAll(warehouses);
                System.out.println("✅ Initialized " + warehouses.size() + " warehouse locations");
            }

            // Initialize sample routes for testing anomaly detection
            if (routeRepository.count() == 0) {
                System.out.println("🛣️ Initializing sample routes...");
                
                // Create a sample route: Cape Town to Stellenbosch
                Route route1 = new Route("ROUTE-001", "TRUCK-001", "WH-001", "WH-002");
                route1.setStatus("ACTIVE");
                routeRepository.save(route1);
                
                // Add waypoints for route 1 (Cape Town to Stellenbosch waypoints)
                List<RouteWaypoint> waypoints1 = Arrays.asList(
                    new RouteWaypoint("ROUTE-001", 1, -33.9249, 18.4241, 0),      // Cape Town
                    new RouteWaypoint("ROUTE-001", 2, -33.9280, 18.4500, 10),     // Kloof Nek
                    new RouteWaypoint("ROUTE-001", 3, -33.9300, 18.5000, 20),     // Signal Hill
                    new RouteWaypoint("ROUTE-001", 4, -33.9310, 18.5500, 30),     // Sea Point
                    new RouteWaypoint("ROUTE-001", 5, -33.9315, 18.6000, 40),    // Mouille Point
                    new RouteWaypoint("ROUTE-001", 6, -33.9320, 18.6500, 50),     // Green Point
                    new RouteWaypoint("ROUTE-001", 7, -33.9325, 18.7000, 60),     // Maitland
                    new RouteWaypoint("ROUTE-001", 8, -33.9328, 18.7500, 70),     // Bellville
                    new RouteWaypoint("ROUTE-001", 9, -33.9328, 18.8000, 80),     // Stikland
                    new RouteWaypoint("ROUTE-001", 10, -33.9328, 18.8601, 90)     // Stellenbosch
                );
                waypointRepository.saveAll(waypoints1);
                
                // Create a second sample route
                Route route2 = new Route("ROUTE-002", "TRUCK-002", "WH-001", "WH-003");
                route2.setStatus("ACTIVE");
                routeRepository.save(route2);
                
                // Add waypoints for route 2 (Cape Town to Paarl)
                List<RouteWaypoint> waypoints2 = Arrays.asList(
                    new RouteWaypoint("ROUTE-002", 1, -33.9249, 18.4241, 0),     // Cape Town
                    new RouteWaypoint("ROUTE-002", 2, -33.9000, 18.5000, 15),    // Wynberg
                    new RouteWaypoint("ROUTE-002", 3, -33.8500, 18.6000, 30),    // Plumstead
                    new RouteWaypoint("ROUTE-002", 4, -33.8000, 18.7000, 45),    // Kraaifontein
                    new RouteWaypoint("ROUTE-002", 5, -33.7500, 18.8000, 60),    // Durbanville
                    new RouteWaypoint("ROUTE-002", 6, -33.7167, 18.9667, 75)     // Paarl
                );
                waypointRepository.saveAll(waypoints2);
                
                System.out.println("✅ Initialized 2 sample routes with waypoints");
            }
        };
    }
}

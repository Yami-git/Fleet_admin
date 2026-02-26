package com.logistics.fleet_backend.config;

import com.logistics.fleet_backend.model.Location;
import com.logistics.fleet_backend.repository.LocationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(LocationRepository locationRepository) {
        return args -> {
            // Only initialize if no locations exist
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
            } else {
                System.out.println("📦 Database already contains " + locationRepository.count() + " locations");
            }
        };
    }
}

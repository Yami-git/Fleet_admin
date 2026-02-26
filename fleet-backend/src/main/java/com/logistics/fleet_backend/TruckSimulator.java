package com.logistics.fleet_backend;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Random;

/**
 * Multi-threaded Truck Simulator
 * Simulates 1000+ trucks concurrently using Java ExecutorService
 * Demonstrates concurrency skills vital for Java/C++ roles
 */
public class TruckSimulator {

    // Configuration
    private static final int NUM_TRUCKS = 1000;
    private static final int THREAD_POOL_SIZE = 100;
    private static final String BASE_URL = "http://localhost:8080/api/vehicles";
    
    // Cape Town area (starting point for all trucks with slight variations)
    private static final double BASE_LAT = -33.9249;
    private static final double BASE_LON = 18.4241;

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("🚚 MULTI-THREADED TRUCK SIMULATOR 🚚");
        System.out.println("===========================================");
        System.out.println("Simulating " + NUM_TRUCKS + " trucks with " + THREAD_POOL_SIZE + " threads");
        System.out.println("===========================================\n");

        // Create a fixed thread pool
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        // Submit tasks for each truck
        for (int i = 0; i < NUM_TRUCKS; i++) {
            final String truckId = "TRUCK-" + String.format("%04d", i);
            executor.submit(() -> simulateTruck(truckId));
        }

        // Keep the main thread alive to allow simulated trucks to run
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n🛑 Shutting down simulator...");
            executor.shutdown();
        }));
    }

    /**
     * Simulates a single truck moving independently
     * Each truck has its own position and movement pattern
     */
    private static void simulateTruck(String truckId) {
        RestTemplate restTemplate = new RestTemplate();
        Random random = new Random();
        
        // Each truck starts at a slightly different position (spread across Cape Town)
        double lat = BASE_LAT + (random.nextDouble() - 0.5) * 0.1;
        double lon = BASE_LON + (random.nextDouble() - 0.5) * 0.1;
        
        // Each truck has slightly different speed
        double latSpeed = 0.0002 + (random.nextDouble() * 0.0003);
        double lonSpeed = 0.0002 + (random.nextDouble() * 0.0003);

        // Random initial delay to stagger the trucks
        try {
            TimeUnit.MILLISECONDS.sleep(random.nextInt(2000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("🚚 " + truckId + " started at " + lat + ", " + lon);

        while (!Thread.currentThread().isInterrupted()) {
            try {
                // Move the truck
                lat += latSpeed;
                lon += lonSpeed;

                // Add some randomness to movement (simulate traffic, route changes)
                if (random.nextDouble() > 0.95) {
                    latSpeed = -latSpeed;
                }
                if (random.nextDouble() > 0.95) {
                    lonSpeed = -lonSpeed;
                }

                // Build JSON payload
                String jsonPayload = String.format(
                    "{\"truckId\": \"%s\", \"latitude\": %.6f, \"longitude\": %.6f}",
                    truckId, lat, lon
                );

                // Send update via REST API
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

                restTemplate.postForObject(BASE_URL, request, String.class);

                // Each truck updates at slightly different intervals (0.5-2 seconds)
                TimeUnit.MILLISECONDS.sleep(500 + random.nextInt(1500));

            } catch (Exception e) {
                System.err.println("❌ " + truckId + " Error: " + e.getMessage());
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}

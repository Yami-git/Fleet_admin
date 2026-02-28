package com.logistics.fleet_backend;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.concurrent.*;

public class FleetSimulator {

    private static final int NUM_TRUCKS = 10;
    private static final int THREAD_POOL_SIZE = 10;
    private static final String API_URL = "http://localhost:8080/api/vehicles";

    public static void main(String[] args) {
        System.out.println("🚚 STARTING FLEET SIMULATION - " + NUM_TRUCKS + " TRUCKS");

        // Create fixed thread pool with 100 threads
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        // Submit all truck tasks
        for (int i = 1; i <= NUM_TRUCKS; i++) {
            String truckId = String.format("TRUCK-%04d", i);
            executor.submit(new TruckSimulatorTask(truckId, API_URL));
        }

        // Keep running until interrupted
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("🛑 Shutting down fleet simulator...");
            executor.shutdown();
            try {
                executor.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }));

        System.out.println("✅ Fleet simulation running with " + NUM_TRUCKS + " trucks");
    }
}

class TruckSimulatorTask implements Runnable {
    private final String truckId;
    private final String apiUrl;
    private final RestTemplate restTemplate;

    public TruckSimulatorTask(String truckId, String apiUrl) {
        this.truckId = truckId;
        this.apiUrl = apiUrl;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void run() {
        // Each truck starts at different location (spread across Cape Town area)
        double baseLat = -33.9 + (Math.random() * 0.1);
        double baseLon = 18.4 + (Math.random() * 0.1);

        while (!Thread.currentThread().isInterrupted()) {
            try {
                // Random movement pattern
                baseLat += (Math.random() - 0.5) * 0.001;
                baseLon += (Math.random() - 0.5) * 0.001;

                String json = String.format(
                    "{\"truckId\": \"%s\", \"latitude\": %.6f, \"longitude\": %.6f}",
                    truckId, baseLat, baseLon
                );

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> request = new HttpEntity<>(json, headers);

                restTemplate.postForObject(apiUrl, request, String.class);

                // Random interval between 1-5 seconds per truck
                Thread.sleep(1000 + (long)(Math.random() * 4000));

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("❌ Error for " + truckId + ": " + e.getMessage());
            }
        }
    }
}

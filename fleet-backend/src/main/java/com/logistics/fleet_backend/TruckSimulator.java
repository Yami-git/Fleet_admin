package com.logistics.fleet_backend;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.concurrent.TimeUnit;

public class TruckSimulator {

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/vehicles";

        double lat = -33.9249;
        double lon = 18.4241;
        String truckId = "SIMULATED-TRUCK-01";

        System.out.println("🚚 STARTING TRUCK SIMULATOR...");

        while (true) {
            try {
                // Move logic
                lat += 0.0005;
                lon += 0.0005;

                // FIX: Manually build the JSON string
                // This creates: {"truckId": "SIMULATED-TRUCK-01", "latitude": -33.xxx, "longitude": 18.xxx}
                String jsonPayload = "{"
                        + "\"truckId\": \"" + truckId + "\","
                        + "\"latitude\": " + lat + ","
                        + "\"longitude\": " + lon
                        + "}";

                // Set headers
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                // Send the String, not the Map
                HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

                restTemplate.postForObject(url, request, String.class);

                System.out.println("📍 Sent Update: " + lat + ", " + lon);

                TimeUnit.SECONDS.sleep(2);

            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
                try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException ex) { }
            }
        }
    }
}
package com.logistics.fleet_backend.controller;

import com.logistics.fleet_backend.model.Vehicle;
import com.logistics.fleet_backend.model.RouteDeviation;
import com.logistics.fleet_backend.repository.VehicleRepository;
import com.logistics.fleet_backend.service.AnomalyDetectionService;
import com.logistics.fleet_backend.service.VehicleCacheService;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.kafka.core.KafkaTemplate;


import java.util.*;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController{
    private final VehicleRepository repository;
    private final VehicleCacheService cacheService;
    private final SimpMessagingTemplate messagingTemplate;
    private final AnomalyDetectionService anomalyDetectionService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "vehicle-updates";
    
    public VehicleController(VehicleRepository repository, 
                           SimpMessagingTemplate messagingTemplate,
                           AnomalyDetectionService anomalyDetectionService, VehicleCacheService cacheService,
                           KafkaTemplate<String, Object> kafkaTemplate) {
        System.out.println("--------------------------------------");
        System.out.println("🚨 VEHICLE CONTROLLER IS LOADED 🚨");
        System.out.println("--------------------------------------");
        this.repository=repository;
        this.cacheService = cacheService;
        this.messagingTemplate=messagingTemplate;
        this.anomalyDetectionService=anomalyDetectionService;
        this.kafkaTemplate=kafkaTemplate;

    }
    // New test endpoint
    @GetMapping("/ping")
    public String ping() {
        return "Pong! Server is alive.";
    }

    @GetMapping
    public List<Vehicle>getAllVehicles(){
        // Option 1: Return from cache if available
        // For full list, still query DB but could paginate
        
        // Option 2: For single vehicle, check cache first
        return repository.findAll();
    }
    @GetMapping("/{truckId}")
    public Vehicle getVehicle(@PathVariable String truckId){
        //Try cache first
        Vehicle cached = cacheService.getCachedVehicle(truckId);
        if(cached != null){
            return cached;
        }

        //Fallback to DB
        return repository.findById(truckId).orElse(null);

    }

    @PostMapping
    public Vehicle updateLocation(@RequestBody Vehicle vehicle){
        //Save to database (Persistent)
        Vehicle savedVehicle=repository.save(vehicle);

        // Send to Kafka topic (async, non-blocking)
        kafkaTemplate.send(TOPIC, vehicle.getTruckId(), vehicle);
        

        //Also save to cache (Fast access)
        cacheService.cacheVehicleLocation(savedVehicle);

        // Send location update via WebSocket
        String destination="/topic/updates";
        messagingTemplate.convertAndSend(destination,savedVehicle);
        
        // Check for route deviation
        RouteDeviation deviation = anomalyDetectionService.checkDeviation(
            vehicle.getTruckId(),
            vehicle.getLatitude(),
            vehicle.getLongitude()
        );
        
        // If deviation detected, it will be sent via WebSocket automatically
        // by the AnomalyDetectionService
        
        return savedVehicle;
    }
}

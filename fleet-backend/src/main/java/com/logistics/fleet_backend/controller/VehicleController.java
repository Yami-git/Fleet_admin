package com.logistics.fleet_backend.controller;

import com.logistics.fleet_backend.model.Vehicle;
import com.logistics.fleet_backend.model.RouteDeviation;
import com.logistics.fleet_backend.repository.VehicleRepository;
import com.logistics.fleet_backend.service.AnomalyDetectionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;


import java.util.*;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController{
    private final VehicleRepository repository;
    private final SimpMessagingTemplate messagingTemplate;
    private final AnomalyDetectionService anomalyDetectionService;
    
    public VehicleController(VehicleRepository repository, 
                           SimpMessagingTemplate messagingTemplate,
                           AnomalyDetectionService anomalyDetectionService){
        System.out.println("--------------------------------------");
        System.out.println("🚨 VEHICLE CONTROLLER IS LOADED 🚨");
        System.out.println("--------------------------------------");
        this.repository=repository;
        this.messagingTemplate=messagingTemplate;
        this.anomalyDetectionService=anomalyDetectionService;

    }
    // New test endpoint
    @GetMapping("/ping")
    public String ping() {
        return "Pong! Server is alive.";
    }

    @GetMapping
    public List<Vehicle>getAllVehicles(){
        return repository.findAll();
    }

    @PostMapping
    public Vehicle updateLocation(@RequestBody Vehicle vehicle){
        Vehicle savedVehicle=repository.save(vehicle);
        
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

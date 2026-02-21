package com.logistics.fleet_backend.controller;

import com.logistics.fleet_backend.model.Vehicle;
import com.logistics.fleet_backend.repository.VehicleRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;


import java.util.*;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController{
    private final VehicleRepository repository;
    private final SimpMessagingTemplate messagingTemplate;
    public VehicleController(VehicleRepository repository, SimpMessagingTemplate messagingTemplate){
        System.out.println("--------------------------------------");
        System.out.println("🚨 VEHICLE CONTROLLER IS LOADED 🚨");
        System.out.println("--------------------------------------");
        this.repository=repository;
        this.messagingTemplate=messagingTemplate;

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
        String Destination="/topic/updates";
        messagingTemplate.convertAndSend(Destination,savedVehicle);

        return savedVehicle;
    }
}
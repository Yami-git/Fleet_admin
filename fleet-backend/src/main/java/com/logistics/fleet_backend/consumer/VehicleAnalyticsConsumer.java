package com.logistics.fleet_backend.consumer;

import com.logistics.fleet_backend.model.Vehicle;
import com.logistics.fleet_backend.service.AnomalyDetectionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class VehicleAnalyticsConsumer {
    private final AnomalyDetectionService anomalyDetectionService;
    private final SimpMessagingTemplate messagingTemplate; //The megaphone


    //Inject the messaging template via the constructor
    public VehicleAnalyticsConsumer(AnomalyDetectionService anomalyDetectionService, SimpMessagingTemplate messagingTemplate) {
        this.anomalyDetectionService = anomalyDetectionService;
        this.messagingTemplate=messagingTemplate;
    }

    @KafkaListener(topics = "vehicle-updates", groupId = "fleet-group")
    public void consumeVehicleUpdate(Vehicle vehicle) {
        System.out.println("Received vehicle update for truck: " + vehicle.getTruckId());

        //Run anomaly detection (background processing)
        anomalyDetectionService.checkDeviation(vehicle.getTruckId(),vehicle.getLatitude(), vehicle.getLongitude());

        // Could add more analytics here:
        // - Speed calculation
        // - Route progress
        // - ETA predictions
        //2. BROADCAST TO REACT MAP!
        messagingTemplate.convertAndSend("/topic/updates", vehicle);
        
    }
}

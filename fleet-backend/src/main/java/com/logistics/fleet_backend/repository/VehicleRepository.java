package com.logistics.fleet_backend.repository;

import com.logistics.fleet_backend.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String>{

    /**
     * Find all vehicles within a given radius (in meters) of a point
     * Uses PostGIS ST_DWithin for efficient spatial query
     */
    @Query(value = "SELECT * FROM vehicle WHERE ST_DWithin(location, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), :distance)", nativeQuery = true)
    List<Vehicle> findWithinRadius(@Param("latitude") double latitude, 
                                    @Param("longitude") double longitude, 
                                    @Param("distance") double distanceMeters);

    /**
     * Find nearest vehicle to a given point
     */
    @Query(value = "SELECT * FROM vehicle ORDER BY location <-> ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326) LIMIT 1", nativeQuery = true)
    Vehicle findNearestVehicle(@Param("latitude") double latitude, 
                                @Param("longitude") double longitude);

}

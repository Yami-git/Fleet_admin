package com.logistics.fleet_backend.repository;

import com.logistics.fleet_backend.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, String>{

    @Query(value = "SELECT * FROM location WHERE ST_DWithin(location, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), :distance)", nativeQuery = true)
    List<Location> findWithinRadius(@Param("latitude") double latitude, 
                                    @Param("longitude") double longitude, 
                                    @Param("distance") double distanceMeters);
}

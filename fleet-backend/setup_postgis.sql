-- PostGIS Database Setup Script for Fleet Logistics
-- Run this script to set up the database with PostGIS extension

-- Create the database (if not exists)
-- CREATE DATABASE fleet_logistics;

-- Enable PostGIS extension
CREATE EXTENSION IF NOT EXISTS postgis;

-- Create location column for Vehicle table (spatial indexing)
-- This adds a geometry column for PostGIS queries
ALTER TABLE vehicle ADD COLUMN IF NOT EXISTS location geometry(Point, 4326);

-- Create spatial index for faster proximity queries
CREATE INDEX IF NOT EXISTS vehicle_location_idx ON vehicle USING GIST(location);

-- Create location column for Location table
ALTER TABLE location ADD COLUMN IF NOT EXISTS location geometry(Point, 4326);

-- Create spatial index for location table
CREATE INDEX IF NOT EXISTS location_location_idx ON location USING GIST(location);

-- Function to update location geometry from lat/lon coordinates
CREATE OR REPLACE FUNCTION update_vehicle_location()
RETURNS TRIGGER AS $$
BEGIN
    NEW.location := ST_SetSRID(ST_MakePoint(NEW.longitude, NEW.latitude), 4326);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to automatically update geometry when lat/lon changes
DROP TRIGGER IF EXISTS vehicle_location_trigger ON vehicle;
CREATE TRIGGER vehicle_location_trigger
    BEFORE INSERT OR UPDATE ON vehicle
    FOR EACH ROW
    EXECUTE FUNCTION update_vehicle_location();

-- Similar function and trigger for Location table
CREATE OR REPLACE FUNCTION update_location_geometry()
RETURNS TRIGGER AS $$
BEGIN
    NEW.location := ST_SetSRID(ST_MakePoint(NEW.longitude, NEW.latitude), 4326);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS location_geometry_trigger ON location;
CREATE TRIGGER location_geometry_trigger
    BEFORE INSERT OR UPDATE ON location
    FOR EACH ROW
    EXECUTE FUNCTION update_location_geometry();

-- Verify PostGIS is installed
SELECT postgis_full_version();

-- Sample queries to test:
-- Find vehicles within 5km of a point:
-- SELECT * FROM vehicle WHERE ST_DWithin(location, ST_SetSRID(ST_MakePoint(18.4241, -33.9249), 4326), 5000);

-- Find nearest vehicle to a point:
-- SELECT * FROM vehicle ORDER BY location <-> ST_SetSRID(ST_MakePoint(18.4241, -33.9249), 4326) LIMIT 1;

-- Enable PostGIS extension
CREATE EXTENSION IF NOT EXISTS postgis;

-- Add spatial column to Location table
ALTER TABLE location ADD COLUMN geom geography(POINT, 4326);

-- Update geometry for existing rows
UPDATE location SET geom = ST_SetSRID(
    ST_MakePoint(longitude, latitude), 4326
)::geography;

-- Create spatial index
CREATE INDEX idx_location_geom ON location USING GIST(geom);

-- For vehicle proximity queries
-- Find all warehouses within 5km of a point:
SELECT * FROM location
WHERE ST_DWithin(
    geom,
    ST_MakePoint(18.4241, -33.9249)::geography,
    5000  -- 5km in meters
);

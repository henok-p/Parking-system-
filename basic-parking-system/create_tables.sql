\c postgres

-- Create database if it doesn't exist
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'parking_db') THEN
        CREATE DATABASE parking_db;
    END IF;
END
$$;

\c parking_db

-- Create table if it doesn't exist
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'parking_spots') THEN
        CREATE TABLE parking_spots (
            id VARCHAR(50) PRIMARY KEY,
            is_occupied BOOLEAN DEFAULT FALSE,
            vehicle_id VARCHAR(50)
        );
    END IF;
END
$$;

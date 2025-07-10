package com.parking.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initializeDatabase() {
        try {
            // First try to connect to parking_db directly
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:1998/parking_db",
                    "postgres",
                    "H3enok5@11"
                );
                
                try (Statement stmt = conn.createStatement()) {
                    // Try to create table if it doesn't exist
                    stmt.execute("CREATE TABLE IF NOT EXISTS parking_spots (" +
                        "id VARCHAR(50) PRIMARY KEY," +
                        "is_occupied BOOLEAN DEFAULT FALSE," +
                        "vehicle_id VARCHAR(50)"
                    + ")");
                    
                    // Insert initial parking spots if none exist
                    stmt.execute("SELECT COUNT(*) FROM parking_spots");
                    ResultSet rs = stmt.getResultSet();
                    if (rs.next() && rs.getInt(1) == 0) {
                        for (char c = 'A'; c <= 'J'; c++) {
                            stmt.execute("INSERT INTO parking_spots (id) VALUES ('" + c + "1')");
                        }
                    }
                }
            } catch (Exception e) {
                // If we can't connect to parking_db, create it
                conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:1998/postgres",
                    "postgres",
                    "H3enok5@11"
                );
                
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("CREATE DATABASE parking_db");
                }
                
                // Now connect to the new database
                conn.close();
                conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:1998/parking_db",
                    "postgres",
                    "H3enok5@11"
                );
                
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("CREATE TABLE parking_spots (" +
                        "id VARCHAR(50) PRIMARY KEY," +
                        "is_occupied BOOLEAN DEFAULT FALSE," +
                        "vehicle_id VARCHAR(50)"
                    + ")");
                    
                    // Insert initial parking spots
                    for (char c = 'A'; c <= 'J'; c++) {
                        stmt.execute("INSERT INTO parking_spots (id) VALUES ('" + c + "1')");
                    }
                }
            } finally {
                if (conn != null) {
                    conn.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}

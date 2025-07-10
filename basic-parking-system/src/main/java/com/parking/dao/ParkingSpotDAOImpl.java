package com.parking.dao;

import com.parking.db.DatabaseManager;
import com.parking.model.ParkingSpot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParkingSpotDAOImpl implements ParkingSpotDAO {
    private final DatabaseManager dbManager = DatabaseManager.getInstance();

    @Override
    public void createSpot(ParkingSpot spot) throws SQLException {
        if (spot == null || spot.getId() == null || spot.getId().isEmpty()) {
            throw new IllegalArgumentException("Spot and ID cannot be null or empty");
        }
        
        String sql = "INSERT INTO parking_spots (id, is_occupied, vehicle_id) VALUES (?, ?, ?)";
        try (Connection conn = dbManager.getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, spot.getId());
                pstmt.setBoolean(2, spot.isOccupied());
                pstmt.setString(3, spot.getVehicleId());
                pstmt.executeUpdate();
            }
        }
    }

    @Override
    public ParkingSpot getSpotById(String id) throws SQLException {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        
        String sql = "SELECT * FROM parking_spots WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new ParkingSpot(
                    rs.getString("id"),
                    rs.getBoolean("is_occupied"),
                    rs.getString("vehicle_id")
                );
            }
            return null;
        }
    }

    @Override
    public List<ParkingSpot> getAllSpots() throws SQLException {
        List<ParkingSpot> spots = new ArrayList<>();
        String sql = "SELECT * FROM parking_spots";
        try (Connection conn = dbManager.getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    spots.add(new ParkingSpot(
                        rs.getString("id"),
                        rs.getBoolean("is_occupied"),
                        rs.getString("vehicle_id")
                    ));
                }
                return spots;
            }
        }
    }

    @Override
    public void updateSpot(ParkingSpot spot) throws SQLException {
        if (spot == null || spot.getId() == null || spot.getId().isEmpty()) {
            throw new IllegalArgumentException("Spot and ID cannot be null or empty");
        }
        
        String sql = "UPDATE parking_spots SET is_occupied = ?, vehicle_id = ? WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, spot.isOccupied());
            pstmt.setString(2, spot.getVehicleId());
            pstmt.setString(3, spot.getId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deleteSpot(String id) throws SQLException {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        
        String sql = "DELETE FROM parking_spots WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<ParkingSpot> getAvailableSpots() {
        String sql = "SELECT * FROM parking_spots WHERE is_occupied = false";
        List<ParkingSpot> spots = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                spots.add(new ParkingSpot(
                    rs.getString("id"),
                    rs.getBoolean("is_occupied"),
                    rs.getString("vehicle_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spots;
    }

    @Override
    public List<ParkingSpot> getOccupiedSpots() {
        String sql = "SELECT * FROM parking_spots WHERE is_occupied = true";
        List<ParkingSpot> spots = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                spots.add(new ParkingSpot(
                    rs.getString("id"),
                    rs.getBoolean("is_occupied"),
                    rs.getString("vehicle_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spots;
    }
}

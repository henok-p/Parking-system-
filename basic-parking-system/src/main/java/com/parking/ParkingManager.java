package com.parking;

import com.parking.dao.ParkingSpotDAO;
import com.parking.dao.ParkingSpotDAOImpl;
import com.parking.model.ParkingSpot;
import java.sql.SQLException;
import java.util.List;

public class ParkingManager {
    private final ParkingSpotDAO parkingSpotDAO;

    public ParkingManager() {
        this.parkingSpotDAO = new ParkingSpotDAOImpl();
    }

    public void initializeParkingSpots(int numberOfSpots) {
        if (numberOfSpots <= 0) {
            throw new IllegalArgumentException("Number of spots must be positive");
        }
        
        try {
            List<ParkingSpot> existingSpots = parkingSpotDAO.getAllSpots();
            if (existingSpots.size() < numberOfSpots) {
                // Only create new spots if we don't have enough
                int spotsToCreate = numberOfSpots - existingSpots.size();
                for (int i = 1; i <= spotsToCreate; i++) {
                    String spotId = "A" + (existingSpots.size() + i);
                    ParkingSpot spot = new ParkingSpot(spotId);
                    parkingSpotDAO.createSpot(spot);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize parking spots", e);
        }
    }

    public boolean parkVehicle(String spotId, String vehicleId) throws SQLException {
        if (spotId == null || spotId.isEmpty() || vehicleId == null || vehicleId.isEmpty()) {
            throw new IllegalArgumentException("Spot ID and Vehicle ID cannot be null or empty");
        }
        
        ParkingSpot spot = parkingSpotDAO.getSpotById(spotId);
        if (spot == null) {
            throw new RuntimeException("Spot not found: " + spotId);
        }
        if (spot.isOccupied()) {
            throw new RuntimeException("Spot already occupied: " + spotId);
        }
        
        spot.setOccupied(true);
        spot.setVehicleId(vehicleId);
        parkingSpotDAO.updateSpot(spot);
        return true;
    }

    public boolean unparkVehicle(String spotId) throws SQLException {
        if (spotId == null || spotId.isEmpty()) {
            throw new IllegalArgumentException("Spot ID cannot be null or empty");
        }
        
        ParkingSpot spot = parkingSpotDAO.getSpotById(spotId);
        if (spot == null) {
            throw new RuntimeException("Spot not found: " + spotId);
        }
        if (!spot.isOccupied()) {
            throw new RuntimeException("Spot is not occupied: " + spotId);
        }
        
        spot.setOccupied(false);
        spot.setVehicleId(null);
        parkingSpotDAO.updateSpot(spot);
        return true;
    }

    public List<ParkingSpot> getAvailableSpots() throws SQLException {
        return parkingSpotDAO.getAvailableSpots();
    }

    public List<ParkingSpot> getOccupiedSpots() throws SQLException {
        return parkingSpotDAO.getOccupiedSpots();
    }

    public List<ParkingSpot> getAllSpots() throws SQLException {
        return parkingSpotDAO.getAllSpots();
    }

    public int getTotalAvailableSpotsCount() throws SQLException {
        return getAvailableSpots().size();
    }
}

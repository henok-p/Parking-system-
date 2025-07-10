package com.parking.dao;

import com.parking.model.ParkingSpot;
import java.sql.SQLException;
import java.util.List;

public interface ParkingSpotDAO {
    void createSpot(ParkingSpot spot) throws SQLException;
    ParkingSpot getSpotById(String id) throws SQLException;
    List<ParkingSpot> getAllSpots() throws SQLException;
    void updateSpot(ParkingSpot spot) throws SQLException;
    void deleteSpot(String id) throws SQLException;
    List<ParkingSpot> getAvailableSpots() throws SQLException;
    List<ParkingSpot> getOccupiedSpots() throws SQLException;
}

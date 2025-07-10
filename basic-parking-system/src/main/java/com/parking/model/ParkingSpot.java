package com.parking.model;

public class ParkingSpot {
    private String id;
    private boolean isOccupied;
    private String vehicleId;

    public ParkingSpot(String id) {
        this.id = id;
        this.isOccupied = false;
        this.vehicleId = null;
    }

    public ParkingSpot(String id, boolean isOccupied, String vehicleId) {
        this.id = id;
        this.isOccupied = isOccupied;
        this.vehicleId = vehicleId;
    }

    public String getId() {
        return id;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setOccupied(boolean occupied) {
        this.isOccupied = occupied;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    @Override
    public String toString() {
        return "ParkingSpot{" +
                "id='" + id + '\'' +
                ", isOccupied=" + isOccupied +
                ", vehicleId='" + vehicleId + '\'' +
                '}';
    }
}

package com.parking.ui;

import com.parking.ParkingManager;
import com.parking.model.ParkingSpot;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.border.TitledBorder;

public class ParkingUI extends JFrame {
    private final ParkingManager parkingManager;
    private JTable spotsTable;
    private DefaultTableModel tableModel;
    private JTextField vehicleIdField;
    private JComboBox<String> parkSpotComboBox;
    private JComboBox<String> unparkSpotComboBox;
    private JLabel statusLabel;
    private JPanel parkPanel;
    private GridBagConstraints gbc = new GridBagConstraints();

    public ParkingUI() {
        parkingManager = new ParkingManager();
        initializeUI();
        initializeParkingSpots();
        updateSpotList();
    }

    private void initializeUI() {
        setTitle("Modern Parking Management System");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // Create main panel with a modern layout
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create parking section with modern styling
        parkPanel = new JPanel(new BorderLayout(10, 10));
        parkPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185)),
            "Park Vehicle",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(41, 128, 185)
        ));
        
        JPanel parkControls = new JPanel(new GridBagLayout());
        
        JLabel vehicleLabel = new JLabel("Vehicle ID:");
        vehicleIdField = new JTextField(15);
        vehicleIdField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel spotLabel = new JLabel("Available Spots:");
        parkSpotComboBox = new JComboBox<>();
        parkSpotComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        updateParkSpotComboBox();
        
        JButton parkButton = new JButton("Park Vehicle");
        parkButton.setFont(new Font("Arial", Font.BOLD, 14));
        parkButton.setBackground(new Color(41, 128, 185));
        parkButton.setForeground(Color.WHITE);
        parkButton.addActionListener(e -> {
            String vehicleId = vehicleIdField.getText().trim();
            String spotId = (String) parkSpotComboBox.getSelectedItem();
            
            if (vehicleId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a vehicle ID", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (spotId == null) {
                JOptionPane.showMessageDialog(this, "No available spots", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                if (parkingManager.parkVehicle(spotId, vehicleId)) {
                    JOptionPane.showMessageDialog(this, "Vehicle parked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    updateSpotList();
                    vehicleIdField.setText("");
                    updateStatus();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to park vehicle. Spot may be occupied or invalid.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, "Application error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Add components to parking panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        parkControls.add(vehicleLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        parkControls.add(vehicleIdField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        parkControls.add(spotLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        parkControls.add(parkSpotComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        parkControls.add(parkButton, gbc);
        
        parkPanel.add(parkControls, BorderLayout.CENTER);

        // Create table for parking spots
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Spot ID");
        tableModel.addColumn("Status");
        tableModel.addColumn("Vehicle ID");
        spotsTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        spotsTable.setFillsViewportHeight(true);
        spotsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Create unpark section with modern styling
        JPanel unparkPanel = new JPanel(new BorderLayout(10, 10));
        unparkPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(192, 57, 43)),
            "Unpark Vehicle",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(192, 57, 43)
        ));
        
        JPanel unparkControls = new JPanel(new GridBagLayout());
        
        JLabel unparkLabel = new JLabel("Occupied Spots:");
        unparkLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        unparkSpotComboBox = new JComboBox<>();
        unparkSpotComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        updateUnparkSpotComboBox();
        
        JButton unparkButton = new JButton("Unpark Vehicle");
        unparkButton.setFont(new Font("Arial", Font.BOLD, 14));
        unparkButton.setBackground(new Color(192, 57, 43));
        unparkButton.setForeground(Color.WHITE);
        unparkButton.addActionListener(e -> {
            String spotId = (String) unparkSpotComboBox.getSelectedItem();
            if (spotId != null) {
                try {
                    if (parkingManager.unparkVehicle(spotId)) {
                        JOptionPane.showMessageDialog(this, "Vehicle unparked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        updateSpotList();
                        updateUnparkSpotComboBox();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to unpark vehicle", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(this, "Application error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Add components to unpark panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        unparkControls.add(unparkLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        unparkControls.add(unparkSpotComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        unparkControls.add(unparkButton, gbc);
        
        unparkPanel.add(unparkControls, BorderLayout.CENTER);

        // Add components to main panel
        mainPanel.add(parkPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(spotsTable), BorderLayout.CENTER);
        mainPanel.add(unparkPanel, BorderLayout.SOUTH);

        // Add status label at bottom
        statusLabel = new JLabel("Status: Ready");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(statusLabel, BorderLayout.PAGE_END);

        add(mainPanel);
        setVisible(true);
    }

    private void updateParkSpotComboBox() {
        try {
            List<ParkingSpot> spots = parkingManager.getAllSpots();
            List<String> availableSpots = spots.stream()
                .filter(spot -> !spot.isOccupied())
                .map(ParkingSpot::getId)
                .collect(Collectors.toList());
            
            parkSpotComboBox.removeAllItems();
            for (String spot : availableSpots) {
                parkSpotComboBox.addItem(spot);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to update available spots: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUnparkSpotComboBox() {
        try {
            List<ParkingSpot> spots = parkingManager.getAllSpots();
            List<String> occupiedSpots = spots.stream()
                .filter(spot -> spot.isOccupied())
                .map(spot -> spot.getId() + " (" + spot.getVehicleId() + ")")
                .collect(Collectors.toList());
            
            unparkSpotComboBox.removeAllItems();
            for (String spot : occupiedSpots) {
                unparkSpotComboBox.addItem(spot);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to update occupied spots: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void updateStatus() {
        try {
            int available = parkingManager.getTotalAvailableSpotsCount();
            int total = parkingManager.getAllSpots().size();
            statusLabel.setText("Status: " + available + " of " + total + " spots available");
        } catch (SQLException e) {
            statusLabel.setText("Status: Error fetching status");
        }
    }



    private void initializeParkingSpots() {
        try {
            parkingManager.initializeParkingSpots(10);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to initialize parking spots: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSpotList() {
        try {
            List<ParkingSpot> spots = parkingManager.getAllSpots();
            tableModel.setRowCount(0);
            
            for (ParkingSpot spot : spots) {
                tableModel.addRow(new Object[]{
                    spot.getId(),
                    spot.isOccupied() ? "Occupied" : "Available",
                    spot.getVehicleId()
                });
            }
            updateParkSpotComboBox();
            updateUnparkSpotComboBox();
            updateStatus();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to update spot list: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ParkingUI::new);
    }
}

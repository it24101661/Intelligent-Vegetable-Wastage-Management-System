package com.example.delivery_and_distribution.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "delivery_agents")
public class DeliveryAgent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agent_name", nullable = false)
    private String agentName;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "vehicle_number")
    private String vehicleNumber;

    @Column(name = "max_capacity")
    private Double maxCapacity;

    @Column(name = "current_capacity")
    private Double currentCapacity = 0.0;

    @Column(name = "status")
    private String status = "AVAILABLE";

    @Column(name = "current_location_lat")
    private Double currentLocationLat;

    @Column(name = "current_location_lng")
    private Double currentLocationLng;

    @OneToMany(mappedBy = "assignedAgent")
    private List<DeliveryAssignment> deliveries;

    // Constructors
    public DeliveryAgent() {}

    public DeliveryAgent(String agentName, String phoneNumber, String email,
                         String vehicleType, String vehicleNumber, Double maxCapacity) {
        this.agentName = agentName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.vehicleType = vehicleType;
        this.vehicleNumber = vehicleNumber;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = 0.0;
        this.status = "AVAILABLE";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public Double getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(Double maxCapacity) { this.maxCapacity = maxCapacity; }

    public Double getCurrentCapacity() { return currentCapacity; }
    public void setCurrentCapacity(Double currentCapacity) { this.currentCapacity = currentCapacity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getCurrentLocationLat() { return currentLocationLat; }
    public void setCurrentLocationLat(Double currentLocationLat) { this.currentLocationLat = currentLocationLat; }

    public Double getCurrentLocationLng() { return currentLocationLng; }
    public void setCurrentLocationLng(Double currentLocationLng) { this.currentLocationLng = currentLocationLng; }

    public List<DeliveryAssignment> getDeliveries() { return deliveries; }
    public void setDeliveries(List<DeliveryAssignment> deliveries) { this.deliveries = deliveries; }
}
package com.example.delivery_and_distribution.dto;



public class DeliveryAgentDTO {
    private Long id;
    private String agentName;
    private String phoneNumber;
    private String email;
    private String vehicleType;
    private String vehicleNumber;
    private Double maxCapacity;
    private Double currentCapacity;
    private String status;

    // Constructors
    public DeliveryAgentDTO() {}

    public DeliveryAgentDTO(Long id, String agentName, String phoneNumber, String email,
                            String vehicleType, String vehicleNumber, Double maxCapacity,
                            Double currentCapacity, String status) {
        this.id = id;
        this.agentName = agentName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.vehicleType = vehicleType;
        this.vehicleNumber = vehicleNumber;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;
        this.status = status;
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
}

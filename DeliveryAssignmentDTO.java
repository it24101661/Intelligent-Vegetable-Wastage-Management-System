package com.example.delivery_and_distribution.dto;



import java.time.LocalDateTime;

public class DeliveryAssignmentDTO {
    private Long id;
    private Long agentId;
    private String agentName;
    private Long orderId;
    private String orderNumber;
    private String customerName;
    private String customerAddress;
    private LocalDateTime estimatedDeliveryTime;
    private String deliveryStatus;
    private String deliveryNotes;

    // Constructors
    public DeliveryAssignmentDTO() {}

    public DeliveryAssignmentDTO(Long id, Long agentId, String agentName, Long orderId,
                                 String orderNumber, String customerName, String customerAddress,
                                 LocalDateTime estimatedDeliveryTime, String deliveryStatus,
                                 String deliveryNotes) {
        this.id = id;
        this.agentId = agentId;
        this.agentName = agentName;
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.deliveryStatus = deliveryStatus;
        this.deliveryNotes = deliveryNotes;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAgentId() { return agentId; }
    public void setAgentId(Long agentId) { this.agentId = agentId; }

    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerAddress() { return customerAddress; }
    public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }

    public LocalDateTime getEstimatedDeliveryTime() { return estimatedDeliveryTime; }
    public void setEstimatedDeliveryTime(LocalDateTime estimatedDeliveryTime) { this.estimatedDeliveryTime = estimatedDeliveryTime; }

    public String getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }

    public String getDeliveryNotes() { return deliveryNotes; }
    public void setDeliveryNotes(String deliveryNotes) { this.deliveryNotes = deliveryNotes; }
}

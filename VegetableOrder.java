package com.example.delivery_and_distribution.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vegetable_orders")
public class VegetableOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", unique = true, nullable = false)
    private String orderNumber;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "customer_phone", nullable = false)
    private String customerPhone;

    @Column(name = "customer_address", nullable = false)
    private String customerAddress;

    @Column(name = "delivery_lat")
    private Double deliveryLat;

    @Column(name = "delivery_lng")
    private Double deliveryLng;

    @Column(name = "vegetable_type", nullable = false)
    private String vegetableType;

    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Column(name = "status")
    private String status = "PENDING";

    @Column(name = "total_amount")
    private Double totalAmount;

    @OneToOne(mappedBy = "order")
    private DeliveryAssignment deliveryAssignment;

    // Constructors
    public VegetableOrder() {}

    public VegetableOrder(String orderNumber, String customerName, String customerPhone,
                          String customerAddress, String vegetableType, Double quantity) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.vegetableType = vegetableType;
        this.quantity = quantity;
        this.orderDate = LocalDateTime.now();
        this.status = "PENDING";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCustomerAddress() { return customerAddress; }
    public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }

    public Double getDeliveryLat() { return deliveryLat; }
    public void setDeliveryLat(Double deliveryLat) { this.deliveryLat = deliveryLat; }

    public Double getDeliveryLng() { return deliveryLng; }
    public void setDeliveryLng(Double deliveryLng) { this.deliveryLng = deliveryLng; }

    public String getVegetableType() { return vegetableType; }
    public void setVegetableType(String vegetableType) { this.vegetableType = vegetableType; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public LocalDateTime getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(LocalDateTime deliveryDate) { this.deliveryDate = deliveryDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public DeliveryAssignment getDeliveryAssignment() { return deliveryAssignment; }
    public void setDeliveryAssignment(DeliveryAssignment deliveryAssignment) { this.deliveryAssignment = deliveryAssignment; }
}

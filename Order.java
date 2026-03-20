package com.example.veg.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "`order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    private String status;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private java.time.LocalDateTime updatedAt;

    // Frontend sends these - ignore them (not in DB)
    @Transient
    private Object items;

    @Transient
    private String deliveryAddress;

    @Transient
    private String notes;

    public Order() {
        this.status = "PENDING";
        this.orderDate = LocalDate.now();
        this.createdAt = java.time.LocalDateTime.now();
        this.updatedAt = java.time.LocalDateTime.now();
        this.totalPrice = 0;
    }


    // Getters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public LocalDate getOrderDate() { return orderDate; }
    public LocalDate getDeliveryDate() { return deliveryDate; }
    public String getStatus() { return status; }
    public int getTotalPrice() { return totalPrice; }
    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public java.time.LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setBuyerId(Long buyerId) { this.userId = buyerId; }
    public Long getBuyerId() { return this.userId; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
    public void setDeliveryDate(LocalDate deliveryDate) { this.deliveryDate = deliveryDate; }
    public void setStatus(String status) { this.status = status; }
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }
    public void setItems(Object items) { this.items = items; }
    public Object getItems() { return items; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getNotes() { return notes; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(java.time.LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
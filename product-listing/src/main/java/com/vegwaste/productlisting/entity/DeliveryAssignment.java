
package com.vegwaste.productlisting.entity;



import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_assignments")
public class DeliveryAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false)
    private DeliveryAgent assignedAgent;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private VegetableOrder order;

    @Column(name = "assignment_date")
    private LocalDateTime assignmentDate;

    @Column(name = "estimated_delivery_time")
    private LocalDateTime estimatedDeliveryTime;

    @Column(name = "actual_delivery_time")
    private LocalDateTime actualDeliveryTime;

    @Column(name = "delivery_status")
    private String deliveryStatus = "PENDING";

    @Column(name = "delivery_notes")
    private String deliveryNotes;

    @Column(name = "assigned_by")
    private String assignedBy;

    // Constructors
    public DeliveryAssignment() {
        this.assignmentDate = LocalDateTime.now();
    }

    public DeliveryAssignment(DeliveryAgent assignedAgent, VegetableOrder order,
                              LocalDateTime estimatedDeliveryTime, String assignedBy) {
        this.assignedAgent = assignedAgent;
        this.order = order;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.assignedBy = assignedBy;
        this.assignmentDate = LocalDateTime.now();
        this.deliveryStatus = "PENDING";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public DeliveryAgent getAssignedAgent() { return assignedAgent; }
    public void setAssignedAgent(DeliveryAgent assignedAgent) { this.assignedAgent = assignedAgent; }

    public VegetableOrder getOrder() { return order; }
    public void setOrder(VegetableOrder order) { this.order = order; }

    public LocalDateTime getAssignmentDate() { return assignmentDate; }
    public void setAssignmentDate(LocalDateTime assignmentDate) { this.assignmentDate = assignmentDate; }

    public LocalDateTime getEstimatedDeliveryTime() { return estimatedDeliveryTime; }
    public void setEstimatedDeliveryTime(LocalDateTime estimatedDeliveryTime) { this.estimatedDeliveryTime = estimatedDeliveryTime; }

    public LocalDateTime getActualDeliveryTime() { return actualDeliveryTime; }
    public void setActualDeliveryTime(LocalDateTime actualDeliveryTime) { this.actualDeliveryTime = actualDeliveryTime; }

    public String getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }

    public String getDeliveryNotes() { return deliveryNotes; }
    public void setDeliveryNotes(String deliveryNotes) { this.deliveryNotes = deliveryNotes; }

    public String getAssignedBy() { return assignedBy; }
    public void setAssignedBy(String assignedBy) { this.assignedBy = assignedBy; }
}

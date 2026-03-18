package com.vegwaste.productlisting.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "farmer_stock")
@Data
public class FarmerStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stockId;

    private Integer farmerId;
    private String vegetableName;
    private String category;
    private Float quantityKg;
    private Float pricePerKg;
    private LocalDate harvestDate;
    private Integer shelfLifeDays;
    private Integer freshnessAgeDays;

    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum AvailabilityStatus {
        available, low, out_of_stock
    }
}
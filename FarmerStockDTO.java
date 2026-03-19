package org.example.farmer_stock_management.DTO;

import java.time.LocalDate;

public class FarmerStockDTO {
    private String farmerId;
    private String vegetableName;
    private String category;
    private LocalDate harvestDate;
    private Double quantityKg;
    private Double pricePerKg;
    private String qualityGrade;
    private LocalDate expiryEstimate;

    // Constructors
    public FarmerStockDTO() {}

    public FarmerStockDTO(String farmerId, String vegetableName, String category,
                          LocalDate harvestDate, Double quantityKg, Double pricePerKg,
                          String qualityGrade, LocalDate expiryEstimate) {
        this.farmerId = farmerId;
        this.vegetableName = vegetableName;
        this.category = category;
        this.harvestDate = harvestDate;
        this.quantityKg = quantityKg;
        this.pricePerKg = pricePerKg;
        this.qualityGrade = qualityGrade;
        this.expiryEstimate = expiryEstimate;
    }

    // Getters and Setters
    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public String getVegetableName() {
        return vegetableName;
    }

    public void setVegetableName(String vegetableName) {
        this.vegetableName = vegetableName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getHarvestDate() {
        return harvestDate;
    }

    public void setHarvestDate(LocalDate harvestDate) {
        this.harvestDate = harvestDate;
    }

    public Double getQuantityKg() {
        return quantityKg;
    }

    public void setQuantityKg(Double quantityKg) {
        this.quantityKg = quantityKg;
    }

    public Double getPricePerKg() {
        return pricePerKg;
    }

    public void setPricePerKg(Double pricePerKg) {
        this.pricePerKg = pricePerKg;
    }

    public String getQualityGrade() {
        return qualityGrade;
    }

    public void setQualityGrade(String qualityGrade) {
        this.qualityGrade = qualityGrade;
    }

    public LocalDate getExpiryEstimate() {
        return expiryEstimate;
    }

    public void setExpiryEstimate(LocalDate expiryEstimate) {
        this.expiryEstimate = expiryEstimate;
    }
}
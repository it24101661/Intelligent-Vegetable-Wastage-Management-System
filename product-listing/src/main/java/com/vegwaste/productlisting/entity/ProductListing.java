package com.vegwaste.productlisting.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_listing")
public class ProductListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "listing_id")
    private Integer listingId;

    @Column(name = "farmer_id")
    private Integer farmerId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "is_visible")
    private Boolean isVisible;

    @Column(name = "listed_at")
    private LocalDateTime listedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private FarmerStock farmerStock;
    @Column(name = "risk_level")
    private String riskLevel; // HIGH, MEDIUM, LOW

    @Column(name = "suggested_discount")
    private Integer suggestedDiscount; // percentage like 20, 30, 50

    // Add getters and setters
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public Integer getSuggestedDiscount() { return suggestedDiscount; }
    public void setSuggestedDiscount(Integer discount) { this.suggestedDiscount = discount; }

    public Integer getListingId() { return listingId; }
    public void setListingId(Integer id) { this.listingId = id; }
    public Integer getFarmerId() { return farmerId; }
    public void setFarmerId(Integer farmerId) { this.farmerId = farmerId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getIsVisible() { return isVisible; }
    public void setIsVisible(Boolean isVisible) { this.isVisible = isVisible; }
    public LocalDateTime getListedAt() { return listedAt; }
    public void setListedAt(LocalDateTime listedAt) { this.listedAt = listedAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public FarmerStock getFarmerStock() { return farmerStock; }
    public void setFarmerStock(FarmerStock f) { this.farmerStock = f; }
    public Integer getStockId() {
        return farmerStock != null ? farmerStock.getStockId() : null;
    }
}
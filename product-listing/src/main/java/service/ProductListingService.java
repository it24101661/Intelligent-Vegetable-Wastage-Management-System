package com.vegwaste.productlisting.service;

import com.vegwaste.productlisting.entity.ProductListing;
import com.vegwaste.productlisting.repository.ProductListingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductListingService {

    @Autowired
    private ProductListingRepository repository;

    public ProductListing createListing(ProductListing listing) {
        listing.setListedAt(LocalDateTime.now());
        listing.setIsVisible(true);
        return repository.save(listing);
    }

    public List<ProductListing> getAllListings() {
        return repository.findAll();
    }

    public Optional<ProductListing> getListingById(Integer id) {
        return repository.findById(id);
    }

    public List<ProductListing> getListingsByVisibility(Boolean isVisible) {
        return repository.findByIsVisible(isVisible);
    }

    public List<ProductListing> searchListings(String keyword) {
        return repository.findByTitleContainingIgnoreCase(keyword);
    }

    public List<ProductListing> getListingsByFarmer(Integer farmerId) {
        return repository.findByFarmerId(farmerId);
    }

    public List<ProductListing> getHighRiskListings() {
        return repository.findAll().stream()
                .filter(l -> "HIGH".equals(l.getRiskLevel()))
                .collect(Collectors.toList());
    }

    public List<ProductListing> getListingsSortedByRisk() {
        return repository.findAll().stream()
                .sorted(Comparator.comparingInt(
                        l -> -getRiskScore(((ProductListing) l).getRiskLevel())
                ))
                .collect(Collectors.toList());
    }

    private int getRiskScore(String risk) {
        if (risk == null) return 0;
        switch (risk) {
            case "HIGH": return 3;
            case "MEDIUM": return 2;
            case "LOW": return 1;
            default: return 0;
        }
    }

    public ProductListing updateVisibility(Integer id, Boolean isVisible) {
        ProductListing listing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found: " + id));
        listing.setIsVisible(isVisible);
        return repository.save(listing);
    }

    public ProductListing updateListing(Integer id, ProductListing updatedListing) {
        ProductListing listing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found: " + id));
        listing.setTitle(updatedListing.getTitle());
        listing.setDescription(updatedListing.getDescription());
        listing.setIsVisible(updatedListing.getIsVisible());
        listing.setExpiresAt(updatedListing.getExpiresAt());
        return repository.save(listing);
    }

    @Transactional
    public String removeExpiredListings() {
        List<ProductListing> expired = repository.findExpiredListings(LocalDateTime.now());
        int count = expired.size();
        repository.deleteExpiredListings(LocalDateTime.now());
        return count + " expired listing(s) removed.";
    }

    public void deleteListing(Integer id) {
        repository.deleteById(id);
    }
}
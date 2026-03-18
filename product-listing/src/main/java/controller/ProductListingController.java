package com.vegwaste.productlisting.controller;

import com.vegwaste.productlisting.entity.ProductListing;
import com.vegwaste.productlisting.service.ProductListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/listings")
@CrossOrigin(origins = "*")
public class ProductListingController {

    @Autowired
    private ProductListingService service;

    // CREATE
    @PostMapping
    public ResponseEntity<ProductListing> createListing(@RequestBody ProductListing listing) {
        return ResponseEntity.ok(service.createListing(listing));
    }

    // READ - all
    @GetMapping
    public ResponseEntity<List<ProductListing>> getAllListings() {
        return ResponseEntity.ok(service.getAllListings());
    }

    // READ - by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductListing> getById(@PathVariable Integer id) {
        return service.getListingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // READ - visible only
    @GetMapping("/filter/visible")
    public ResponseEntity<List<ProductListing>> getVisible() {
        return ResponseEntity.ok(service.getListingsByVisibility(true));
    }

    // READ - search
    @GetMapping("/search")
    public ResponseEntity<List<ProductListing>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(service.searchListings(keyword));
    }

    // READ - by farmer
    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<ProductListing>> getByFarmer(@PathVariable Integer farmerId) {
        return ResponseEntity.ok(service.getListingsByFarmer(farmerId));
    }

    // READ - high risk
    @GetMapping("/risk/high")
    public ResponseEntity<List<ProductListing>> getHighRisk() {
        return ResponseEntity.ok(service.getHighRiskListings());
    }

    // READ - sorted by risk
    @GetMapping("/sorted-by-risk")
    public ResponseEntity<List<ProductListing>> getSortedByRisk() {
        return ResponseEntity.ok(service.getListingsSortedByRisk());
    }

    // UPDATE - visibility
    @PatchMapping("/{id}/visibility")
    public ResponseEntity<ProductListing> updateVisibility(
            @PathVariable Integer id,
            @RequestParam Boolean isVisible) {
        return ResponseEntity.ok(service.updateVisibility(id, isVisible));
    }

    // UPDATE - full update
    @PutMapping("/{id}")
    public ResponseEntity<ProductListing> updateListing(
            @PathVariable Integer id,
            @RequestBody ProductListing listing) {
        return ResponseEntity.ok(service.updateListing(id, listing));
    }

    // DELETE - expired
    @DeleteMapping("/expired")
    public ResponseEntity<String> removeExpired() {
        return ResponseEntity.ok(service.removeExpiredListings());
    }

    // DELETE - single
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(@PathVariable Integer id) {
        service.deleteListing(id);
        return ResponseEntity.noContent().build();
    }
}
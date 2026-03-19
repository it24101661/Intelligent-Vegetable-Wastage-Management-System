package org.example.farmer_stock_management.Service;

import org.example.farmer_stock_management.Model.FarmerStock;
import org.example.farmer_stock_management.DTO.FarmerStockDTO;
import org.example.farmer_stock_management.Repository.FarmerStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FarmerStockService {

    @Autowired
    private FarmerStockRepository farmerStockRepository;

    // ==================== CREATE OPERATIONS ====================

    /**
     * Add newly harvested vegetables to the stock
     * @param stockDTO Data Transfer Object containing vegetable details
     * @return The saved FarmerStock object
     */
    public FarmerStock addNewStock(FarmerStockDTO stockDTO) {
        // Validate input
        if (stockDTO.getFarmerId() == null || stockDTO.getFarmerId().isEmpty()) {
            throw new IllegalArgumentException("Farmer ID is required");
        }
        if (stockDTO.getVegetableName() == null || stockDTO.getVegetableName().isEmpty()) {
            throw new IllegalArgumentException("Vegetable name is required");
        }
        if (stockDTO.getQuantityKg() == null || stockDTO.getQuantityKg() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (stockDTO.getPricePerKg() == null || stockDTO.getPricePerKg() <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }

        // Create new FarmerStock object
        FarmerStock farmerStock = new FarmerStock();
        farmerStock.setFarmerId(stockDTO.getFarmerId());
        farmerStock.setVegetableName(stockDTO.getVegetableName());
        farmerStock.setCategory(stockDTO.getCategory());
        farmerStock.setHarvestDate(stockDTO.getHarvestDate());
        farmerStock.setQuantityKg(stockDTO.getQuantityKg());
        farmerStock.setPricePerKg(stockDTO.getPricePerKg());
        farmerStock.setQualityGrade(stockDTO.getQualityGrade());
        farmerStock.setExpiryEstimate(stockDTO.getExpiryEstimate());
        farmerStock.setAvailabilityStatus("Available");
        farmerStock.setCreatedAt(LocalDateTime.now());
        farmerStock.setUpdatedAt(LocalDateTime.now());

        // Save to database
        return farmerStockRepository.save(farmerStock);
    }

    // ==================== READ OPERATIONS ====================

    /**
     * Get all stocks for a farmer
     */
    public List<FarmerStock> getAllStocksByFarmerId(String farmerId) {
        if (farmerId == null || farmerId.isEmpty()) {
            throw new IllegalArgumentException("Farmer ID is required");
        }
        return farmerStockRepository.findByFarmerId(farmerId);
    }

    /**
     * Get all available stocks (not sold out)
     */
    public List<FarmerStock> getAllAvailableStocks() {
        return farmerStockRepository.findAll()
                .stream()
                .filter(stock -> !stock.getAvailabilityStatus().equals("Out of Stock"))
                .collect(Collectors.toList());
    }

    /**
     * Get stocks by farmer ID and category
     */
    public List<FarmerStock> getStocksByFarmerAndCategory(String farmerId, String category) {
        if (farmerId == null || farmerId.isEmpty()) {
            throw new IllegalArgumentException("Farmer ID is required");
        }
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Category is required");
        }
        return farmerStockRepository.findByFarmerIdAndCategory(farmerId, category);
    }

    /**
     * Search stocks by vegetable name (case-insensitive)
     */
    public List<FarmerStock> searchStocksByVegetableName(String farmerId, String vegetableName) {
        if (farmerId == null || farmerId.isEmpty()) {
            throw new IllegalArgumentException("Farmer ID is required");
        }
        if (vegetableName == null || vegetableName.isEmpty()) {
            throw new IllegalArgumentException("Vegetable name is required");
        }
        return getAllStocksByFarmerId(farmerId)
                .stream()
                .filter(stock -> stock.getVegetableName()
                        .toLowerCase()
                        .contains(vegetableName.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Get stock by ID
     */
    public FarmerStock getStockById(Integer stockId) {
        return farmerStockRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found with ID: " + stockId));
    }

    /**
     * Get low stock items (quantity < 10 kg)
     */
    public List<FarmerStock> getLowStockItems(String farmerId) {
        return getAllStocksByFarmerId(farmerId)
                .stream()
                .filter(stock -> stock.getQuantityKg() < 10)
                .collect(Collectors.toList());
    }

    /**
     * Get critical spoilage risk items
     */
    public List<FarmerStock> getCriticalSpoilageRiskItems(String farmerId) {
        return getAllStocksByFarmerId(farmerId)
                .stream()
                .filter(stock -> "Critical".equals(calculateSpoilageRisk(stock.getExpiryEstimate()))
                        || "Expired".equals(calculateSpoilageRisk(stock.getExpiryEstimate())))
                .collect(Collectors.toList());
    }

    // ==================== SEARCH & SORT OPERATIONS ====================

    /**
     * Calculate spoilage risk based on expiry date
     * Returns: "Critical" (< 2 days), "High" (2-5 days), "Medium" (5-10 days), "Low" (> 10 days)
     */
    public String calculateSpoilageRisk(LocalDate expiryDate) {
        if (expiryDate == null) {
            return "Unknown";
        }

        long daysUntilExpiry = ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);

        if (daysUntilExpiry < 0) {
            return "Expired";
        } else if (daysUntilExpiry < 2) {
            return "Critical";
        } else if (daysUntilExpiry < 5) {
            return "High";
        } else if (daysUntilExpiry < 10) {
            return "Medium";
        } else {
            return "Low";
        }
    }

    /**
     * Get stocks sorted by harvest date (newest first)
     */
    public List<FarmerStock> getStocksSortedByHarvestDate(String farmerId) {
        return getAllStocksByFarmerId(farmerId)
                .stream()
                .sorted(Comparator.comparing(FarmerStock::getHarvestDate).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Get stocks sorted by quantity (highest first)
     */
    public List<FarmerStock> getStocksSortedByQuantity(String farmerId) {
        return getAllStocksByFarmerId(farmerId)
                .stream()
                .sorted(Comparator.comparing(FarmerStock::getQuantityKg).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Get stocks sorted by price (highest first)
     */
    public List<FarmerStock> getStocksSortedByPrice(String farmerId) {
        return getAllStocksByFarmerId(farmerId)
                .stream()
                .sorted(Comparator.comparing(FarmerStock::getPricePerKg).reversed())
                .collect(Collectors.toList());
    }

    // ==================== UPDATE OPERATIONS ====================

    /**
     * Update multiple fields at once (Full Update)
     */
    public FarmerStock updateStock(Integer stockId, Double quantity, Double price, String qualityGrade, String status) {
        FarmerStock stock = farmerStockRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found with ID: " + stockId));

        // Update quantity if provided
        if (quantity != null && quantity >= 0) {
            stock.setQuantityKg(quantity);

            // Auto-update status if not manually provided
            if (status == null) {
                if (quantity == 0) {
                    stock.setAvailabilityStatus("Out of Stock");
                } else if (quantity < 10) {
                    stock.setAvailabilityStatus("Low Stock");
                } else {
                    stock.setAvailabilityStatus("Available");
                }
            }
        }

        // Update price if provided
        if (price != null && price > 0) {
            stock.setPricePerKg(price);
        }

        // Update quality grade if provided
        if (qualityGrade != null && !qualityGrade.isEmpty()) {
            if (qualityGrade.matches("[ABC]")) {
                stock.setQualityGrade(qualityGrade);
            }
        }

        // Update status if provided
        if (status != null && !status.isEmpty()) {
            if (status.matches("Available|Low Stock|Out of Stock")) {
                stock.setAvailabilityStatus(status);
            }
        }

        stock.setUpdatedAt(LocalDateTime.now());
        return farmerStockRepository.save(stock);
    }

    /**
     * Update stock quantity and recalculate availability status
     */
    public FarmerStock updateStockQuantity(Integer stockId, Double newQuantity) {
        FarmerStock stock = farmerStockRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found with ID: " + stockId));

        if (newQuantity == null || newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        stock.setQuantityKg(newQuantity);

        // Auto-update availability status based on quantity
        if (newQuantity == 0) {
            stock.setAvailabilityStatus("Out of Stock");
        } else if (newQuantity < 10) {
            stock.setAvailabilityStatus("Low Stock");
        } else {
            stock.setAvailabilityStatus("Available");
        }

        stock.setUpdatedAt(LocalDateTime.now());
        return farmerStockRepository.save(stock);
    }

    /**
     * Update stock price
     */
    public FarmerStock updateStockPrice(Integer stockId, Double newPrice) {
        FarmerStock stock = farmerStockRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found with ID: " + stockId));

        if (newPrice == null || newPrice <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }

        stock.setPricePerKg(newPrice);
        stock.setUpdatedAt(LocalDateTime.now());
        return farmerStockRepository.save(stock);
    }

    /**
     * Update stock quality grade
     */
    public FarmerStock updateStockQuality(Integer stockId, String qualityGrade) {
        FarmerStock stock = farmerStockRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found with ID: " + stockId));

        if (qualityGrade == null || qualityGrade.isEmpty()) {
            throw new IllegalArgumentException("Quality grade is required");
        }

        if (!qualityGrade.matches("[ABC]")) {
            throw new IllegalArgumentException("Quality grade must be A, B, or C");
        }

        stock.setQualityGrade(qualityGrade);
        stock.setUpdatedAt(LocalDateTime.now());
        return farmerStockRepository.save(stock);
    }

    /**
     * Update stock availability status
     */
    public FarmerStock updateStockStatus(Integer stockId, String status) {
        FarmerStock stock = farmerStockRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found with ID: " + stockId));

        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Status is required");
        }

        if (!status.matches("Available|Low Stock|Out of Stock")) {
            throw new IllegalArgumentException("Invalid status. Must be: Available, Low Stock, or Out of Stock");
        }

        stock.setAvailabilityStatus(status);
        stock.setUpdatedAt(LocalDateTime.now());
        return farmerStockRepository.save(stock);
    }

    // ==================== DELETE OPERATIONS ====================

    /**
     * Delete stock by ID (Hard delete - removes from database)
     */
    public void deleteStock(Integer stockId) {
        FarmerStock stock = farmerStockRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found with ID: " + stockId));

        farmerStockRepository.deleteById(stockId);
    }

    /**
     * Delete all stocks for a farmer
     */
    public int deleteAllStocksByFarmerId(String farmerId) {
        if (farmerId == null || farmerId.isEmpty()) {
            throw new IllegalArgumentException("Farmer ID is required");
        }

        List<FarmerStock> stocks = farmerStockRepository.findByFarmerId(farmerId);
        int count = stocks.size();
        farmerStockRepository.deleteAll(stocks);
        return count;
    }

    /**
     * Delete stocks with zero quantity (automatic cleanup)
     */
    public int deleteZeroQuantityStocks(String farmerId) {
        if (farmerId == null || farmerId.isEmpty()) {
            throw new IllegalArgumentException("Farmer ID is required");
        }

        List<FarmerStock> stocks = farmerStockRepository.findByFarmerId(farmerId)
                .stream()
                .filter(stock -> stock.getQuantityKg() == 0)
                .collect(Collectors.toList());

        int count = stocks.size();
        if (count > 0) {
            farmerStockRepository.deleteAll(stocks);
        }
        return count;
    }

    /**
     * Delete stocks that are marked as "Out of Stock"
     */
    public int deleteOutOfStockItems(String farmerId) {
        if (farmerId == null || farmerId.isEmpty()) {
            throw new IllegalArgumentException("Farmer ID is required");
        }

        List<FarmerStock> stocks = farmerStockRepository.findByFarmerId(farmerId)
                .stream()
                .filter(stock -> "Out of Stock".equals(stock.getAvailabilityStatus()))
                .collect(Collectors.toList());

        int count = stocks.size();
        if (count > 0) {
            farmerStockRepository.deleteAll(stocks);
        }
        return count;
    }

    /**
     * Delete expired stocks (based on expiry date)
     */
    public int deleteExpiredStocks(String farmerId) {
        if (farmerId == null || farmerId.isEmpty()) {
            throw new IllegalArgumentException("Farmer ID is required");
        }

        LocalDate today = LocalDate.now();
        List<FarmerStock> stocks = farmerStockRepository.findByFarmerId(farmerId)
                .stream()
                .filter(stock -> stock.getExpiryEstimate() != null && stock.getExpiryEstimate().isBefore(today))
                .collect(Collectors.toList());

        int count = stocks.size();
        if (count > 0) {
            farmerStockRepository.deleteAll(stocks);
        }
        return count;
    }

    // ==================== DELETE PREVIEW OPERATIONS ====================

    /**
     * Get count of stocks to be deleted (for preview)
     */
    public int getZeroQuantityStocksCount(String farmerId) {
        if (farmerId == null || farmerId.isEmpty()) {
            throw new IllegalArgumentException("Farmer ID is required");
        }

        return (int) farmerStockRepository.findByFarmerId(farmerId)
                .stream()
                .filter(stock -> stock.getQuantityKg() == 0)
                .count();
    }

    /**
     * Get count of out-of-stock items
     */
    public int getOutOfStockItemsCount(String farmerId) {
        if (farmerId == null || farmerId.isEmpty()) {
            throw new IllegalArgumentException("Farmer ID is required");
        }

        return (int) farmerStockRepository.findByFarmerId(farmerId)
                .stream()
                .filter(stock -> "Out of Stock".equals(stock.getAvailabilityStatus()))
                .count();
    }

    /**
     * Get count of expired stocks
     */
    public int getExpiredStocksCount(String farmerId) {
        if (farmerId == null || farmerId.isEmpty()) {
            throw new IllegalArgumentException("Farmer ID is required");
        }

        LocalDate today = LocalDate.now();
        return (int) farmerStockRepository.findByFarmerId(farmerId)
                .stream()
                .filter(stock -> stock.getExpiryEstimate() != null && stock.getExpiryEstimate().isBefore(today))
                .count();
    }
}
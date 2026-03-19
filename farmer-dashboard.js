// API Base URL
const API_BASE_URL = 'http://localhost:8081/api/farmer/stocks';

// Global Variables
let allStocks = [];
let filteredStocks = [];
let currentDeleteStockId = null;
let currentEditStockId = null;

// DOM Elements
const farmerId = document.getElementById('farmerId');
const stockTableBody = document.getElementById('stockTableBody');
const loadingSpinner = document.getElementById('loadingSpinner');
const successMessage = document.getElementById('successMessage');
const errorMessage = document.getElementById('errorMessage');
const successText = document.getElementById('successText');
const errorText = document.getElementById('errorText');
const noDataMessage = document.getElementById('noDataMessage');
const deleteModal = document.getElementById('deleteModal');
const editModal = document.getElementById('editModal');
const editForm = document.getElementById('editForm');

// Load Dashboard on Page Load
document.addEventListener('DOMContentLoaded', () => {
    const savedFarmerId = localStorage.getItem('farmerId');
    if (savedFarmerId) {
        farmerId.value = savedFarmerId;
        loadDashboard();
    }
});

/**
 * Load all stocks for the farmer
 */
function loadDashboard() {
    const farmerID = farmerId.value.trim();

    if (!farmerID) {
        showErrorMessage('Please enter a Farmer ID');
        return;
    }

    localStorage.setItem('farmerId', farmerID);
    loadingSpinner.style.display = 'block';
    hideAlerts();

    fetch(`${API_BASE_URL}/farmer/${farmerID}`)
        .then(response => response.json())
        .then(data => {
            loadingSpinner.style.display = 'none';

            if (data.success) {
                allStocks = data.data;
                filteredStocks = [...allStocks];
                displayDashboard();
                displayStocks(filteredStocks);
                showSuccessMessage(`✓ Loaded ${data.count} stock(s)`);
            } else {
                showErrorMessage(data.message || 'Failed to load stocks');
                allStocks = [];
                displayDashboard();
                displayStocks([]);
            }
        })
        .catch(error => {
            loadingSpinner.style.display = 'none';
            console.error('Error:', error);
            showErrorMessage('An error occurred while loading stocks');
        });
}

/**
 * Display dashboard statistics
 */
function displayDashboard() {
    // Total Stocks
    document.getElementById('totalItems').textContent = allStocks.length;

    // Total Quantity
    const totalQuantity = allStocks.reduce((sum, stock) => sum + stock.quantityKg, 0).toFixed(2);
    document.getElementById('totalQuantity').textContent = totalQuantity + ' kg';

    // Available Items
    const availableCount = allStocks.filter(s => s.availabilityStatus === 'Available').length;
    document.getElementById('availableCount').textContent = availableCount;

    // Out of Stock
    const outOfStockCount = allStocks.filter(s => s.availabilityStatus === 'Out of Stock').length;
    document.getElementById('outOfStockCount').textContent = outOfStockCount;

    // Low Stock
    const lowStockCount = allStocks.filter(s => s.quantityKg < 10).length;
    document.getElementById('lowStockCount').textContent = lowStockCount;

    // Critical Spoilage
    const criticalCount = allStocks.filter(s =>
        s.spoilageRisk === 'Critical' || s.spoilageRisk === 'Expired'
    ).length;
    document.getElementById('criticalCount').textContent = criticalCount;

    // Update status cards
    updateStatusOverview();

    // Display Price Overview
    displayPriceOverview();

    // Display Recent Activity
    displayRecentActivity();
}

/**
 * Update status overview cards
 */
function updateStatusOverview() {
    const total = allStocks.length || 1;

    const available = allStocks.filter(s => s.availabilityStatus === 'Available').length;
    const low = allStocks.filter(s => s.availabilityStatus === 'Low Stock').length;
    const outOfStock = allStocks.filter(s => s.availabilityStatus === 'Out of Stock').length;

    document.getElementById('statusAvailable').textContent = available + ' items';
    document.getElementById('statusLow').textContent = low + ' items';
    document.getElementById('statusOut').textContent = outOfStock + ' items';

    document.getElementById('statusAvailableFill').style.width = (available / total * 100) + '%';
    document.getElementById('statusLowFill').style.width = (low / total * 100) + '%';
    document.getElementById('statusOutFill').style.width = (outOfStock / total * 100) + '%';
}

/**
 * Display price overview
 */
function displayPriceOverview() {
    const priceOverview = document.getElementById('priceOverview');

    if (allStocks.length === 0) {
        priceOverview.innerHTML = '<p class="no-data">No stocks available</p>';
        return;
    }

    priceOverview.innerHTML = allStocks.map(stock => `
        <div class="price-item">
            <div>
                <div class="price-item-name">${stock.vegetableName}</div>
                <div class="price-item-quantity">${stock.quantityKg} kg available</div>
            </div>
            <div class="price-item-price">Rs. ${stock.pricePerKg}/kg</div>
        </div>
    `).join('');
}

/**
 * Display recent activity
 */
function displayRecentActivity() {
    const recentActivity = document.getElementById('recentActivity');

    if (allStocks.length === 0) {
        recentActivity.innerHTML = '<p class="no-data">No recent activity</p>';
        return;
    }

    // Sort by updated_at and get last 5
    const recent = [...allStocks]
        .sort((a, b) => new Date(b.updatedAt) - new Date(a.updatedAt))
        .slice(0, 5);

    recentActivity.innerHTML = recent.map(stock => `
        <div class="activity-item">
            <div class="activity-content">
                <div class="activity-title">${stock.vegetableName} - ${stock.category}</div>
                <div class="activity-time">${formatDateTime(stock.updatedAt)}</div>
            </div>
            <div class="activity-value">${stock.quantityKg} kg @ Rs. ${stock.pricePerKg}</div>
        </div>
    `).join('');
}

/**
 * Display stocks in table
 */
function displayStocks(stocks) {
    if (stocks.length === 0) {
        stockTableBody.innerHTML = '';
        noDataMessage.style.display = 'block';
        return;
    }

    noDataMessage.style.display = 'none';

    stockTableBody.innerHTML = stocks.map(stock => `
        <tr>
            <td>${stock.stockId}</td>
            <td>${stock.vegetableName}</td>
            <td>${stock.category}</td>
            <td>${stock.quantityKg} kg</td>
            <td>Rs. ${stock.pricePerKg.toFixed(2)}</td>
            <td><span class="grade-badge grade-${stock.qualityGrade.toLowerCase()}">${stock.qualityGrade}</span></td>
            <td>${formatDate(stock.harvestDate)}</td>
            <td>${stock.expiryEstimate ? formatDate(stock.expiryEstimate) : 'N/A'}</td>
            <td><span class="risk-badge risk-${getSpoilageRiskClass(stock.spoilageRisk)}">${stock.spoilageRisk}</span></td>
            <td><span class="status-badge status-${getStatusClass(stock.availabilityStatus)}">${stock.availabilityStatus}</span></td>
            <td>
                <div class="action-buttons">
                    <button class="btn-small btn-edit" onclick="openEditModal(${stock.stockId}, ${stock.quantityKg}, ${stock.pricePerKg}, '${stock.qualityGrade}', '${stock.availabilityStatus}')">✏️ Edit</button>
                    <button class="btn-small btn-delete" onclick="openDeleteModal(${stock.stockId}, '${stock.vegetableName}', ${stock.quantityKg})">🗑️ Delete</button>
                </div>
            </td>
        </tr>
    `).join('');
}

/**
 * Format date to DD/MM/YYYY
 */
function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString('en-IN');
}

/**
 * Format date time
 */
function formatDateTime(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleString('en-IN');
}

/**
 * Get spoilage risk CSS class
 */
function getSpoilageRiskClass(risk) {
    switch (risk) {
        case 'Critical': return 'critical';
        case 'High': return 'high';
        case 'Medium': return 'medium';
        case 'Low': return 'low';
        case 'Expired': return 'expired';
        default: return 'low';
    }
}

/**
 * Get status CSS class
 */
function getStatusClass(status) {
    switch (status) {
        case 'Available': return 'available';
        case 'Low Stock': return 'low';
        case 'Out of Stock': return 'out';
        default: return 'available';
    }
}

/**
 * Search stocks
 */
function searchStocks() {
    const query = document.getElementById('searchInput').value.trim();

    if (!query) {
        filteredStocks = [...allStocks];
    } else {
        filteredStocks = allStocks.filter(stock =>
            stock.vegetableName.toLowerCase().includes(query.toLowerCase())
        );
    }

    displayStocks(filteredStocks);
}

/**
 * Filter by category
 */
function filterByCategory() {
    const category = document.getElementById('categoryFilter').value;

    if (!category) {
        filteredStocks = [...allStocks];
    } else {
        filteredStocks = allStocks.filter(stock => stock.category === category);
    }

    displayStocks(filteredStocks);
}

/**
 * Sort stocks
 */
function sortStocks() {
    const sortOption = document.getElementById('sortSelect').value;
    const farmerID = farmerId.value.trim();

    if (!sortOption) return;

    loadingSpinner.style.display = 'block';
    hideAlerts();

    let url;
    switch (sortOption) {
        case 'harvest-date':
            url = `${API_BASE_URL}/farmer/${farmerID}/sort/harvest-date`;
            break;
        case 'quantity':
            url = `${API_BASE_URL}/farmer/${farmerID}/sort/quantity`;
            break;
        case 'price':
            url = `${API_BASE_URL}/farmer/${farmerID}/sort/price`;
            break;
        default:
            return;
    }

    fetch(url)
        .then(response => response.json())
        .then(data => {
            loadingSpinner.style.display = 'none';
            if (data.success) {
                allStocks = data.data;
                filteredStocks = [...allStocks];
                displayStocks(filteredStocks);
            } else {
                showErrorMessage(data.message || 'Failed to sort stocks');
            }
        })
        .catch(error => {
            loadingSpinner.style.display = 'none';
            showErrorMessage('An error occurred: ' + error.message);
        });
}

/**
 * Show low stock items
 */
function showLowStock() {
    const farmerID = farmerId.value.trim();

    if (!farmerID) {
        showErrorMessage('Please enter a Farmer ID');
        return;
    }

    loadingSpinner.style.display = 'block';
    hideAlerts();

    fetch(`${API_BASE_URL}/farmer/${farmerID}/low-stock`)
        .then(response => response.json())
        .then(data => {
            loadingSpinner.style.display = 'none';
            if (data.success) {
                allStocks = data.data;
                filteredStocks = [...allStocks];
                displayStocks(filteredStocks);
                showSuccessMessage(`Found ${data.count} low stock item(s)`);
                document.querySelector('.stocks-section').scrollIntoView({ behavior: 'smooth' });
            } else {
                showErrorMessage('No low stock items found');
            }
        })
        .catch(error => {
            loadingSpinner.style.display = 'none';
            showErrorMessage('An error occurred: ' + error.message);
        });
}

/**
 * Show critical spoilage risk items
 */
function showCriticalSpoilage() {
    const farmerID = farmerId.value.trim();

    if (!farmerID) {
        showErrorMessage('Please enter a Farmer ID');
        return;
    }

    loadingSpinner.style.display = 'block';
    hideAlerts();

    fetch(`${API_BASE_URL}/farmer/${farmerID}/critical-spoilage`)
        .then(response => response.json())
        .then(data => {
            loadingSpinner.style.display = 'none';
            if (data.success) {
                allStocks = data.data;
                filteredStocks = [...allStocks];
                displayStocks(filteredStocks);
                if (data.count > 0) {
                    showErrorMessage(`⏰ Found ${data.count} item(s) with critical spoilage risk!`);
                } else {
                    showSuccessMessage('No items with critical spoilage risk');
                }
                document.querySelector('.stocks-section').scrollIntoView({ behavior: 'smooth' });
            } else {
                showErrorMessage('No critical spoilage items found');
            }
        })
        .catch(error => {
            loadingSpinner.style.display = 'none';
            showErrorMessage('An error occurred: ' + error.message);
        });
}

// ==================== DELETE OPERATIONS ====================

/**
 * Open delete confirmation modal
 */
function openDeleteModal(stockId, vegetableName, quantity) {
    currentDeleteStockId = stockId;

    const deleteInfo = document.getElementById('deleteInfo');
    deleteInfo.innerHTML = `
        <div class="info-box">
            <p><strong>🥬 Vegetable:</strong> ${vegetableName}</p>
            <p><strong>⚖️ Quantity:</strong> ${quantity} kg</p>
            <p><strong>ID:</strong> ${stockId}</p>
        </div>
    `;

    deleteModal.style.display = 'block';
}

/**
 * Close delete modal
 */
function closeDeleteModal() {
    deleteModal.style.display = 'none';
    currentDeleteStockId = null;
}

/**
 * Confirm delete
 */
function confirmDelete() {
    if (!currentDeleteStockId) {
        showErrorMessage('Stock ID is missing');
        return;
    }

    deleteStock(currentDeleteStockId);
    closeDeleteModal();
}

/**
 * Delete single stock
 */
function deleteStock(stockId) {
    loadingSpinner.style.display = 'block';
    hideAlerts();

    fetch(`${API_BASE_URL}/${stockId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            loadingSpinner.style.display = 'none';

            if (data.success) {
                showSuccessMessage('✓ Stock deleted successfully');
                setTimeout(() => {
                    loadDashboard();
                }, 1000);
            } else {
                showErrorMessage(data.message || 'Failed to delete stock');
            }
        })
        .catch(error => {
            loadingSpinner.style.display = 'none';
            showErrorMessage('An error occurred: ' + error.message);
        });
}

// ==================== EDIT OPERATIONS ====================

/**
 * Open edit modal
 */
function openEditModal(stockId, quantity, price, quality, status) {
    currentEditStockId = stockId;
    document.getElementById('editQuantity').value = quantity;
    document.getElementById('editPrice').value = price;
    document.getElementById('editQuality').value = quality;
    document.getElementById('editStatus').value = status;
    editModal.style.display = 'block';
}

/**
 * Close edit modal
 */
function closeEditModal() {
    editModal.style.display = 'none';
    currentEditStockId = null;
}

/**
 * Submit edit form
 */
editForm.addEventListener('submit', (e) => {
    e.preventDefault();

    const quantity = parseFloat(document.getElementById('editQuantity').value);
    const price = parseFloat(document.getElementById('editPrice').value);
    const quality = document.getElementById('editQuality').value;
    const status = document.getElementById('editStatus').value;

    if (isNaN(quantity) || quantity < 0) {
        showErrorMessage('Invalid quantity');
        return;
    }
    if (isNaN(price) || price < 0) {
        showErrorMessage('Invalid price');
        return;
    }

    updateStock(currentEditStockId, quantity, price, quality, status);
});

/**
 * Update stock
 */
function updateStock(stockId, quantity, price, quality, status) {
    loadingSpinner.style.display = 'block';
    hideAlerts();
    closeEditModal();

    const updateData = {
        quantityKg: quantity,
        pricePerKg: price,
        qualityGrade: quality,
        availabilityStatus: status
    };

    fetch(`${API_BASE_URL}/${stockId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updateData)
    })
        .then(response => response.json())
        .then(data => {
            loadingSpinner.style.display = 'none';
            if (data.success) {
                showSuccessMessage('✓ Stock updated successfully');
                setTimeout(() => {
                    loadDashboard();
                }, 1000);
            } else {
                showErrorMessage(data.message || 'Failed to update stock');
            }
        })
        .catch(error => {
            loadingSpinner.style.display = 'none';
            showErrorMessage('An error occurred: ' + error.message);
        });
}

// ==================== UTILITY FUNCTIONS ====================

/**
 * Display success message
 */
function showSuccessMessage(message) {
    successText.textContent = message;
    successMessage.style.display = 'flex';
}

/**
 * Display error message
 */
function showErrorMessage(message) {
    errorText.textContent = message;
    errorMessage.style.display = 'flex';
}

/**
 * Hide alerts
 */
function hideAlerts() {
    successMessage.style.display = 'none';
    errorMessage.style.display = 'none';
}

/**
 * Close alert
 */
function closeAlert(alertId) {
    document.getElementById(alertId).style.display = 'none';
}

/**
 * Scroll to section
 */
function scrollToSection(sectionId) {
    const section = document.getElementById(sectionId);
    if (section) {
        section.scrollIntoView({ behavior: 'smooth' });
    }
}

// Close modals when clicking outside
window.addEventListener('click', (event) => {
    if (event.target === deleteModal) {
        closeDeleteModal();
    }
    if (event.target === editModal) {
        closeEditModal();
    }
});
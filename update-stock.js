// API Base URL
const API_BASE_URL = 'http://localhost:8081/api/farmer/stocks';

// DOM Elements
const updateStockForm = document.getElementById('updateStockForm');
const successMessage = document.getElementById('successMessage');
const errorMessage = document.getElementById('errorMessage');
const loadingSpinner = document.getElementById('loadingSpinner');
const successText = document.getElementById('successText');
const errorText = document.getElementById('errorText');
const stockInfo = document.getElementById('stockInfo');

// Global variable to store stock ID
let currentStockId = null;
let currentStockData = null;

// Load stock details on page load
document.addEventListener('DOMContentLoaded', () => {
    // Get stock ID from URL query parameter
    const urlParams = new URLSearchParams(window.location.search);
    currentStockId = urlParams.get('stockId');

    if (!currentStockId) {
        showErrorMessage('Stock ID is required. Please go back and select a stock to update.');
        document.getElementById('updateStockForm').style.display = 'none';
        return;
    }

    // Load stock details
    loadStockDetails(currentStockId);
});

/**
 * Load stock details from API
 */
function loadStockDetails(stockId) {
    loadingSpinner.style.display = 'block';
    hideAlerts();

    fetch(`${API_BASE_URL}/${stockId}`)
        .then(response => response.json())
        .then(data => {
            loadingSpinner.style.display = 'none';

            if (data.success) {
                currentStockData = data.data;
                populateStockInfo();
                populateCurrentValues();
            } else {
                showErrorMessage(data.message || 'Failed to load stock details');
                document.getElementById('updateStockForm').style.display = 'none';
            }
        })
        .catch(error => {
            loadingSpinner.style.display = 'none';
            console.error('Error:', error);
            showErrorMessage('An error occurred while loading stock details: ' + error.message);
            document.getElementById('updateStockForm').style.display = 'none';
        });
}

/**
 * Populate stock info display
 */
function populateStockInfo() {
    document.getElementById('displayVegetable').textContent = currentStockData.vegetableName;
    document.getElementById('displayCategory').textContent = currentStockData.category;
    document.getElementById('displayHarvestDate').textContent = formatDate(currentStockData.harvestDate);
    document.getElementById('displayCreated').textContent = formatDateTime(currentStockData.createdAt);
    stockInfo.style.display = 'block';
}

/**
 * Populate current values in read-only fields
 */
function populateCurrentValues() {
    document.getElementById('currentQuantity').value = currentStockData.quantityKg;
    document.getElementById('currentPrice').value = currentStockData.pricePerKg;
    document.getElementById('currentQuality').value = 'Grade ' + currentStockData.qualityGrade;
    document.getElementById('currentStatus').value = currentStockData.availabilityStatus;
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
 * Form submission
 */
updateStockForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    // Collect form data
    const quantityKg = document.getElementById('newQuantity').value
        ? parseFloat(document.getElementById('newQuantity').value)
        : null;
    const pricePerKg = document.getElementById('newPrice').value
        ? parseFloat(document.getElementById('newPrice').value)
        : null;
    const qualityGrade = document.getElementById('newQuality').value || null;
    const availabilityStatus = document.getElementById('newStatus').value || null;

    // Validate that at least one field is being updated
    if (!quantityKg && !pricePerKg && !qualityGrade && !availabilityStatus) {
        showErrorMessage('Please enter at least one value to update');
        return;
    }

    // Validate values
    if (quantityKg !== null && quantityKg < 0) {
        showErrorMessage('Quantity cannot be negative');
        return;
    }

    if (pricePerKg !== null && pricePerKg <= 0) {
        showErrorMessage('Price must be greater than 0');
        return;
    }

    updateStock(quantityKg, pricePerKg, qualityGrade, availabilityStatus);
});

/**
 * Update stock via API
 */
function updateStock(quantityKg, pricePerKg, qualityGrade, availabilityStatus) {
    loadingSpinner.style.display = 'block';
    hideAlerts();

    const updateData = {
        quantityKg: quantityKg,
        pricePerKg: pricePerKg,
        qualityGrade: qualityGrade,
        availabilityStatus: availabilityStatus
    };

    fetch(`${API_BASE_URL}/${currentStockId}`, {
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
                showSuccessMessage('✓ Stock updated successfully!');
                currentStockData = data.data;
                populateCurrentValues();

                // Reset form
                updateStockForm.reset();

                // Redirect after 2 seconds
                setTimeout(() => {
                    window.location.href = '/farmer-dashboard';
                }, 2000);
            } else {
                showErrorMessage(data.message || 'Failed to update stock');
            }
        })
        .catch(error => {
            loadingSpinner.style.display = 'none';
            console.error('Error:', error);
            showErrorMessage('An error occurred while updating stock: ' + error.message);
        });
}

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
 * Hide alert messages
 */
function hideAlerts() {
    successMessage.style.display = 'none';
    errorMessage.style.display = 'none';
}

/**
 * Close alert message
 */
function closeAlert(alertId) {
    document.getElementById(alertId).style.display = 'none';
}
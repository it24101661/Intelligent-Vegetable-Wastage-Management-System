// API Base URL
const API_BASE_URL = 'http://localhost:8081/api/farmer/stocks';

// DOM Elements
const addStockForm = document.getElementById('addStockForm');
const successMessage = document.getElementById('successMessage');
const errorMessage = document.getElementById('errorMessage');
const loadingSpinner = document.getElementById('loadingSpinner');
const successText = document.getElementById('successText');
const errorText = document.getElementById('errorText');

// Form submission event listener
addStockForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    // Show loading spinner
    loadingSpinner.style.display = 'block';
    hideAlerts();

    // Collect form data
    const formData = {
        farmerId: document.getElementById('farmerId').value.trim(),
        vegetableName: document.getElementById('vegetableName').value.trim(),
        category: document.getElementById('category').value,
        harvestDate: document.getElementById('harvestDate').value,
        quantityKg: parseFloat(document.getElementById('quantityKg').value),
        pricePerKg: parseFloat(document.getElementById('pricePerKg').value),
        qualityGrade: document.querySelector('input[name="qualityGrade"]:checked').value,
        expiryEstimate: document.getElementById('expiryEstimate').value || null
    };

    try {
        // Client-side validation
        validateFormData(formData);

        // Send POST request to backend
        const response = await fetch(`${API_BASE_URL}/add`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });

        const data = await response.json();

        if (response.ok) {
            // Success
            showSuccessMessage(`✓ ${formData.vegetableName} (${formData.quantityKg} kg) added successfully!`);
            addStockForm.reset();

            // Redirect after 2 seconds
            setTimeout(() => {
                window.location.href = '/farmer-dashboard?farmerId=' + formData.farmerId;
            }, 2000);
        } else {
            // API Error
            showErrorMessage(data.message || 'Failed to add stock. Please try again.');
        }
    } catch (error) {
        console.error('Error:', error);
        showErrorMessage(error.message || 'An unexpected error occurred.');
    } finally {
        // Hide loading spinner
        loadingSpinner.style.display = 'none';
    }
});

/**
 * Validate form data on client side
 */
function validateFormData(data) {
    if (!data.farmerId) {
        throw new Error('Farmer ID is required');
    }
    if (!data.vegetableName) {
        throw new Error('Vegetable name is required');
    }
    if (!data.category) {
        throw new Error('Please select a category');
    }
    if (!data.harvestDate) {
        throw new Error('Harvest date is required');
    }
    if (isNaN(data.quantityKg) || data.quantityKg <= 0) {
        throw new Error('Quantity must be a positive number');
    }
    if (isNaN(data.pricePerKg) || data.pricePerKg <= 0) {
        throw new Error('Price must be a positive number');
    }
    if (!data.qualityGrade) {
        throw new Error('Please select a quality grade');
    }

    // Validate dates
    const harvestDate = new Date(data.harvestDate);
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    if (harvestDate > today) {
        throw new Error('Harvest date cannot be in the future');
    }

    if (data.expiryEstimate) {
        const expiryDate = new Date(data.expiryEstimate);
        if (expiryDate < harvestDate) {
            throw new Error('Expiry date cannot be before harvest date');
        }
    }
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

// Set today's date as the default max date for harvest date
document.addEventListener('DOMContentLoaded', () => {
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('harvestDate').setAttribute('max', today);
});
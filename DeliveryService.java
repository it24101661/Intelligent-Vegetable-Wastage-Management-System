package com.example.delivery_and_distribution.service;



import com.example.delivery_and_distribution.dto.AssignOrderRequest;
import com.example.delivery_and_distribution.dto.DeliveryAssignmentDTO;
import com.example.delivery_and_distribution.model.DeliveryAgent;
import com.example.delivery_and_distribution.model.VegetableOrder;

import java.util.List;

public interface DeliveryService {

    // Assignment functions
    DeliveryAssignmentDTO assignOrderToAgent(AssignOrderRequest request) throws Exception;
    List<DeliveryAssignmentDTO> getAllAssignments();
    List<DeliveryAssignmentDTO> getAssignmentsByStatus(String status);
    DeliveryAssignmentDTO updateDeliveryStatus(Long assignmentId, String status, String notes);

    // Agent management
    List<DeliveryAgent> getAvailableAgents();
    DeliveryAgent updateAgentCapacity(Long agentId, Double additionalCapacity);

    // Order management
    List<VegetableOrder> getUnassignedOrders();
    VegetableOrder getOrderDetails(Long orderId);
}

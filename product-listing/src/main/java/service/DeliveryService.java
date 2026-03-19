package service;

import com.vegwaste.productlisting.dto.AssignOrderRequest;
import com.vegwaste.productlisting.dto.DeliveryAssignmentDTO;
import com.vegwaste.productlisting.entity.DeliveryAgent;
import com.vegwaste.productlisting.entity.VegetableOrder;

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

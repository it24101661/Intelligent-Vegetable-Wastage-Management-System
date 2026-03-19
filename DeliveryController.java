package com.example.delivery_and_distribution.controller;



import com.example.delivery_and_distribution.dto.AssignOrderRequest;
import com.example.delivery_and_distribution.dto.DeliveryAssignmentDTO;
import com.example.delivery_and_distribution.model.DeliveryAgent;
import com.example.delivery_and_distribution.model.VegetableOrder;
import com.example.delivery_and_distribution.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    // Page endpoint
    @GetMapping("/assignment")
    public String showAssignmentPage(Model model) {
        model.addAttribute("pageTitle", "Delivery Assignment");
        return "delivery-assignment";
    }

    // API endpoints
    @GetMapping("/api/available-agents")
    @ResponseBody
    public ResponseEntity<List<DeliveryAgent>> getAvailableAgents() {
        return ResponseEntity.ok(deliveryService.getAvailableAgents());
    }

    @GetMapping("/api/unassigned-orders")
    @ResponseBody
    public ResponseEntity<List<VegetableOrder>> getUnassignedOrders() {
        return ResponseEntity.ok(deliveryService.getUnassignedOrders());
    }

    @GetMapping("/api/assignments")
    @ResponseBody
    public ResponseEntity<List<DeliveryAssignmentDTO>> getAllAssignments() {
        return ResponseEntity.ok(deliveryService.getAllAssignments());
    }

    @GetMapping("/api/assignments/status/{status}")
    @ResponseBody
    public ResponseEntity<List<DeliveryAssignmentDTO>> getAssignmentsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(deliveryService.getAssignmentsByStatus(status));
    }

    @PostMapping("/api/assign")
    @ResponseBody
    public ResponseEntity<?> assignOrder(@RequestBody AssignOrderRequest request) {
        try {
            DeliveryAssignmentDTO assignment = deliveryService.assignOrderToAgent(request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Order assigned successfully");
            response.put("assignment", assignment);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/api/assignments/{assignmentId}/status")
    @ResponseBody
    public ResponseEntity<?> updateDeliveryStatus(
            @PathVariable Long assignmentId,
            @RequestParam String status,
            @RequestParam(required = false) String notes) {
        try {
            DeliveryAssignmentDTO assignment = deliveryService.updateDeliveryStatus(assignmentId, status, notes);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Delivery status updated successfully");
            response.put("assignment", assignment);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/api/orders/{orderId}")
    @ResponseBody
    public ResponseEntity<VegetableOrder> getOrderDetails(@PathVariable Long orderId) {
        try {
            return ResponseEntity.ok(deliveryService.getOrderDetails(orderId));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

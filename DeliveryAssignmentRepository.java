package com.example.delivery_and_distribution.repository;



import com.example.delivery_and_distribution.model.DeliveryAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeliveryAssignmentRepository extends JpaRepository<DeliveryAssignment, Long> {

    List<DeliveryAssignment> findByDeliveryStatus(String status);

    List<DeliveryAssignment> findByAssignedAgentId(Long agentId);

    @Query("SELECT d FROM DeliveryAssignment d WHERE d.assignedAgent.id = :agentId AND d.deliveryStatus = :status")
    List<DeliveryAssignment> findByAgentIdAndStatus(@Param("agentId") Long agentId, @Param("status") String status);

    @Query("SELECT d FROM DeliveryAssignment d WHERE d.estimatedDeliveryTime BETWEEN :start AND :end")
    List<DeliveryAssignment> findByEstimatedDeliveryTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(d) > 0 FROM DeliveryAssignment d WHERE d.order.id = :orderId")
    boolean existsByOrderId(@Param("orderId") Long orderId);
}


package com.vegwaste.productlisting.repository;

import com.vegwaste.productlisting.entity.DeliveryAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DeliveryAgentRepository extends JpaRepository<DeliveryAgent, Long> {

    List<DeliveryAgent> findByStatus(String status);

    @Query("SELECT a FROM DeliveryAgent a WHERE a.status = 'AVAILABLE' AND a.currentCapacity < a.maxCapacity")
    List<DeliveryAgent> findAvailableAgents();

    @Query("SELECT a FROM DeliveryAgent a WHERE LOWER(a.agentName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<DeliveryAgent> searchByAgentName(@Param("name") String name);
}


package com.vegwaste.productlisting.repository;

import com.vegwaste.productlisting.entity.VegetableOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VegetableOrderRepository extends JpaRepository<VegetableOrder, Long> {

    List<VegetableOrder> findByStatus(String status);

    @Query("SELECT o FROM VegetableOrder o WHERE o.status = 'PENDING' AND o.id NOT IN (SELECT d.order.id FROM DeliveryAssignment d)")
    List<VegetableOrder> findUnassignedOrders();

    @Query("SELECT o FROM VegetableOrder o WHERE LOWER(o.customerName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<VegetableOrder> searchByCustomerName(@Param("name") String name);
}
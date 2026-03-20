package com.example.veg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.veg.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

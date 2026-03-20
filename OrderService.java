package com.example.veg.service;

import org.springframework.stereotype.Service;
import java.util.List;
import com.example.veg.model.Order;
import com.example.veg.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository repo;

    public OrderService(OrderRepository repo) {
        this.repo = repo;
    }

    public Order save(Order order) {
        return repo.save(order);
    }

    public List<Order> getAll() {
        return repo.findAll();
    }

    // GET BY ID - was missing!
    public Order getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    // UPDATE - was missing!
    public Order update(Long id, Order order) {
        order.setId(id);
        return repo.save(order);
    }

    // DELETE - was missing!
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
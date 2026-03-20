package com.example.veg.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.veg.model.Order;
import com.example.veg.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }


    @PostMapping
    public Order create(@RequestBody Order order) {
        return service.save(order);
    }

    @GetMapping
    public List<Order> getAll() {
        return service.getAll();
    }


    @GetMapping("/{id}")
    public Order getById(@PathVariable Long id) {
        return service.getById(id);
    }


    @PutMapping("/{id}")
    public Order update(@PathVariable Long id, @RequestBody Order order) {
        return service.update(id, order);
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Order deleted successfully!";
    }
}
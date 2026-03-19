package org.example.farmer_stock_management.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    /**
     * Serve index page
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * Serve add-stock page
     */
    @GetMapping("/add-stock")
    public String addStock() {
        return "add-stock";
    }

    /**
     * Serve farmer-dashboard page
     */
    @GetMapping("/farmer-dashboard")
    public String farmerDashboard() {
        return "farmer-dashboard";
    }

    /**
     * Serve update-stock page
     */
    @GetMapping("/update-stock")
    public String updateStock() {
        return "update-stock";
    }
}
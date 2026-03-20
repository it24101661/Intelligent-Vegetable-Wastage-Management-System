package com.example.veg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VegApplication {
    public static void main(String[] args) {
        SpringApplication.run(VegApplication.class, args);
        System.out.println("🚀 App running at: http://localhost:8081");
    }
}
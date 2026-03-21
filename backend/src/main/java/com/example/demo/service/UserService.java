package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    public User register(User user) {
        // Admin gets active by default; Farmer needs approval; Customer is active
        if ("FARMER".equalsIgnoreCase(user.getRole())) {
            user.setStatus("pending");
        } else {
            user.setStatus("active");
        }
        return repo.save(user);
    }

    public User login(String email, String password) {
        User user = repo.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            if ("FARMER".equalsIgnoreCase(user.getRole()) && "pending".equals(user.getStatus())) {
                return null; // not approved yet
            }
            if ("inactive".equals(user.getStatus())) {
                return null;
            }
            return user;
        }
        return null;
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public List<User> getUsersByRole(String role) {
        return repo.findAll().stream()
            .filter(u -> role.equalsIgnoreCase(u.getRole()))
            .collect(Collectors.toList());
    }

    public User getUserById(int id) {
        return repo.findById(id).orElse(null);
    }

    public User updateUser(int id, User updated) {
        User user = repo.findById(id).orElse(null);
        if (user == null) return null;
        user.setName(updated.getName());
        user.setPhone(updated.getPhone());
        user.setNic(updated.getNic());
        user.setFarmSize(updated.getFarmSize());
        user.setFarmLocation(updated.getFarmLocation());
        user.setYearsOfExperience(updated.getYearsOfExperience());
        user.setDeliveryAddress(updated.getDeliveryAddress());
        return repo.save(user);
    }

    public void deleteUser(int id) {
        repo.deleteById(id);
    }

    public void approveUser(int id) {
        User user = repo.findById(id).orElse(null);
        if (user != null) {
            user.setStatus("active");
            repo.save(user);
        }
    }

    public void deactivateUser(int id) {
        User user = repo.findById(id).orElse(null);
        if (user != null) {
            user.setStatus("inactive");
            repo.save(user);
        }
    }
}

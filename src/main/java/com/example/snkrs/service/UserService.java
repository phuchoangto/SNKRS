package com.example.snkrs.service;

import com.example.snkrs.model.Role;
import com.example.snkrs.model.User;
import com.example.snkrs.repository.UserRepository;
import com.example.snkrs.request.SaveUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(SaveUserRequest request) {
        // check username exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // check email exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        var user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        List<Role> roles = new ArrayList<>();
        for (String role : request.getRoles().split(",")) {
            roles.add(Role.valueOf(role));
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public Page<User> getAllUsers(int size, int limit) {
        return userRepository.findAll(PageRequest.of(size, limit));
    }

    public User updateRoles(String id, String roles) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        List<Role> newRoles = new ArrayList<>();
        for (String role : roles.split(",")) {
            newRoles.add(Role.valueOf(role));
        }
        user.setRoles(newRoles);
        return userRepository.save(user);
    }
}

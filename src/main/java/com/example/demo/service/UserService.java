package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    // Registers a new user
    public User registerUser(User user) {
        // Optionally, check for existing username, encrypt password, etc.
        return userRepository.save(user);
    }
    
    // Retrieve user by username (for authentication)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}

package com.example.LAAR.AuthService.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.LAAR.AuthService.Model.User;
import com.example.LAAR.AuthService.Repository.AuthRepository;


@Service
public class AuthService {
    @Autowired
    private AuthRepository authRepository;

    public User registerUser(User user) {
        // Check if user already exists
        User existingUser = authRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("Email already registered");
        }
        user.setPassword(user.getPassword());
        return authRepository.save(user);
    }
}

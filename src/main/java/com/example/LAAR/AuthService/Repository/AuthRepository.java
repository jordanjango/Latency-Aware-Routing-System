package com.example.LAAR.AuthService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.LAAR.AuthService.Model.User;

public interface AuthRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}

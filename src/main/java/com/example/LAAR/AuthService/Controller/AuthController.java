package com.example.LAAR.AuthService.Controller;
import com.example.LAAR.AuthService.Model.User;
import com.example.LAAR.AuthService.Service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody User user){
        User registeredUser = authService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }
}

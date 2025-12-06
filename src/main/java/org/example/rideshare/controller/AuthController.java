package org.example.rideshare.controller;

import jakarta.validation.Valid;
import org.example.rideshare.dto.AuthRequest;
import org.example.rideshare.dto.UserRegisterRequest;
import org.example.rideshare.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegisterRequest req) {
        authService.register(req);
        return ResponseEntity.ok("Registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody AuthRequest req) {
        String token = authService.login(req);
        // If login fails, exceptions are thrown; success returns the JWT token
        return ResponseEntity.ok(token);
    }
}

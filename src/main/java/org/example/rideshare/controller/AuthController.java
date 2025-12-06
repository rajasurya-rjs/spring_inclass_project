package org.example.rideshare.controller;

import jakarta.validation.Valid;
import org.example.rideshare.dto.AuthRequest;
import org.example.rideshare.dto.UserRegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService service) {
        this.authService = service;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegisterRequest req) {
        authService.register(req);
        return ResponseEntity.ok("Registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody AuthRequest req) {
        String token = authService.login(req);
        if (token == null) {
            return ResponseEntity.status(401).body("Invalid username/password");
        }
        return ResponseEntity.ok(token);
    }
}

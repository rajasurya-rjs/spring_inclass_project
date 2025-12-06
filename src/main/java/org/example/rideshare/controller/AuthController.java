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

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegisterRequest req){
        service.register(req);
        return ResponseEntity.ok("Registered");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody AuthRequest req){
        String token = service.login(req);
        if(token == null) return ResponseEntity.status(401).body("Invalid");
        return ResponseEntity.ok(token);
    }
}

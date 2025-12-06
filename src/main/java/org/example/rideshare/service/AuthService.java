package org.example.rideshare.service;

import org.example.rideshare.dto.AuthRequest;
import org.example.rideshare.dto.UserRegisterRequest;
import org.example.rideshare.exception.BadRequestException;
import org.example.rideshare.exception.NotFoundException;
import org.example.rideshare.model.User;
import org.example.rideshare.repository.UserRepository;
import org.example.rideshare.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public void register(UserRegisterRequest req) {
        // Check for duplicate username
        if (repo.existsByUsername(req.getUsername())) {
            throw new BadRequestException("Username already exists");
        }
        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(encoder.encode(req.getPassword()));
        u.setRole(req.getRole()); // "ROLE_USER" or "ROLE_DRIVER"
        repo.save(u);
    }

    public String login(AuthRequest req) {
        User u = repo.findByUsername(req.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!encoder.matches(req.getPassword(), u.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }
        // Generate JWT with username and role
        return JwtUtil.generateToken(u.getUsername(), u.getRole());
    }
}

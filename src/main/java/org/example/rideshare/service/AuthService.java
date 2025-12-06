package org.example.rideshare.service;

import org.example.rideshare.dto.LoginRequest;
import org.example.rideshare.dto.RegisterRequest;
import org.example.rideshare.model.User;
import org.example.rideshare.repository.UserRepository;
import org.example.rideshare.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;

    public AuthService(UserRepository userRepo) {
        this.userRepo = userRepo;
        this.encoder = new BCryptPasswordEncoder();
    }

    public void register(RegisterRequest req) {
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole(req.getRole());
        userRepo.save(user);
    }

    public String login(LoginRequest req) {
        User user = userRepo.findByUsername(req.getUsername());
        if (user != null && encoder.matches(req.getPassword(), user.getPassword())) {
            return JwtUtil.generateToken(user.getUsername(), user.getRole());
        }
        return null;
    }
}

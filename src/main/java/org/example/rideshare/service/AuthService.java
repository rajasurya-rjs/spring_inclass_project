package org.example.rideshare.service;

import org.example.rideshare.dto.AuthRequest;
import org.example.rideshare.dto.UserRegisterRequest;
import org.example.rideshare.exception.NotFoundException;
import org.example.rideshare.model.User;
import org.example.rideshare.repository.UserRepository;
import org.example.rideshare.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder;

    public AuthService(UserRepository repo, BCryptPasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public void register(UserRegisterRequest req){
        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(encoder.encode(req.getPassword()));
        u.setRole(req.getRole());
        repo.save(u);
    }

    public String login(AuthRequest req){
        User u = repo.findByUsername(req.getUsername()).orElse(null);
        if(u == null) throw new NotFoundException("User not found");
        if(!encoder.matches(req.getPassword(), u.getPassword())) return null;
        return JwtUtil.generateToken(u.getUsername(), u.getRole());
    }
}

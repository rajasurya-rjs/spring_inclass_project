package org.example.rideshare.controller;

import jakarta.validation.Valid;
import org.example.rideshare.dto.RideCreateRequest;
import org.example.rideshare.model.Ride;
import org.example.rideshare.repository.RideRepository;
import org.example.rideshare.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Date;

@RestController
@RequestMapping("/api/v1")
public class RideController {

    private final RideRepository repo;

    public RideController(RideRepository r){
        repo = r;
    }

    @PostMapping("/rides")
    public ResponseEntity<Ride> create(@Valid @RequestBody RideCreateRequest req, Authentication auth){
        Ride r = new Ride();
        r.setPickupLocation(req.getPickupLocation());
        r.setDropLocation(req.getDropLocation());
        r.setCreatedAt(new Date());
        r.setStatus("REQUESTED");
        r.setUserId(auth.getName());
        repo.save(r);
        return ResponseEntity.ok(r);
    }


    @GetMapping("/driver/rides/requests")
    public ResponseEntity<List<Ride>> pending(){
        return ResponseEntity.ok(repo.findByStatus("REQUESTED"));
    }

    @PostMapping("/driver/rides/{id}/accept")
    public ResponseEntity<String> accept(@PathVariable String id, Authentication auth){
        Ride r = repo.findById(id).orElse(null);
        if(r == null) return ResponseEntity.notFound().build();
        if(!"REQUESTED".equals(r.getStatus())) return ResponseEntity.badRequest().body("not requested");
        r.setDriverId(auth.getName());
        r.setStatus("ACCEPTED");
        repo.save(r);
        return ResponseEntity.ok("accepted");
    }

    @PostMapping("/rides/{id}/complete")
    public ResponseEntity<String> complete(@PathVariable String id){
        Ride r = repo.findById(id).orElse(null);
        if(r == null) return ResponseEntity.notFound().build();
        if(!"ACCEPTED".equals(r.getStatus())) return ResponseEntity.badRequest().body("not accepted");
        r.setStatus("COMPLETED");
        repo.save(r);
        return ResponseEntity.ok("done");
    }

    @GetMapping("/user/rides")
    public ResponseEntity<List<Ride>> my(Authentication auth){
        return ResponseEntity.ok(repo.findByUserId(auth.getName()));
    }
}

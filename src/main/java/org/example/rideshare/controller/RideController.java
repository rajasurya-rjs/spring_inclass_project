package org.example.rideshare.controller;

import jakarta.validation.Valid;
import org.example.rideshare.dto.RideCreateRequest;
import org.example.rideshare.model.Ride;
import org.example.rideshare.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    // Passenger: create ride
    @PostMapping("/rides")
    public ResponseEntity<Ride> createRide(
            @Valid @RequestBody RideCreateRequest req,
            Authentication auth
    ) {
        Ride r = rideService.createRide(auth.getName(), req);
        return ResponseEntity.ok(r);
    }

    // Passenger: view own rides
    @GetMapping("/user/rides")
    public ResponseEntity<List<Ride>> myRides(Authentication auth) {
        return ResponseEntity.ok(rideService.getUserRides(auth.getName()));
    }

    // Driver: view all pending ride requests
    @GetMapping("/driver/rides/requests")
    public ResponseEntity<List<Ride>> pendingRides() {
        return ResponseEntity.ok(rideService.getPendingRides());
    }

    // Driver: accept ride
    @PostMapping("/driver/rides/accept/{id}")
    public ResponseEntity<?> acceptRide(
            @PathVariable String id,
            Authentication auth
    ) {
        Ride r = rideService.acceptRide(id, auth.getName());
        return ResponseEntity.ok(Map.of(
                "message", "Ride accepted",
                "rideId", r.getId(),
                "status", r.getStatus()
        ));
    }

    // Driver/User: complete ride
    @PostMapping("/rides/{id}/complete")
    public ResponseEntity<?> completeRide(
            @PathVariable String id,
            Authentication auth
    ) {
        Ride r = rideService.completeRide(id);
        return ResponseEntity.ok(Map.of(
                "message", "Ride completed successfully",
                "rideId", r.getId(),
                "status", r.getStatus()
        ));
    }
}

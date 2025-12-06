package org.example.rideshare.controller;

import jakarta.validation.Valid;
import org.example.rideshare.dto.RideCreateRequest;
import org.example.rideshare.model.Ride;
import org.example.rideshare.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    // Request a new ride (passenger)
    @PostMapping("/rides")
    public ResponseEntity<Ride> createRide(@Valid @RequestBody RideCreateRequest req,
                                           Authentication auth) {
        // Authentication.getName() holds the username (user ID)
        Ride r = rideService.createRide(auth.getName(), req);
        return ResponseEntity.ok(r);
    }

    // Driver: view all pending (requested) rides
    @GetMapping("/driver/rides/requests")
    public ResponseEntity<List<Ride>> getPendingRides() {
        List<Ride> list = rideService.getPendingRides();
        return ResponseEntity.ok(list);
    }

    // Driver: accept a ride request
    @PostMapping("/driver/rides/{id}/accept")
    public ResponseEntity<String> acceptRide(@PathVariable String id, Authentication auth) {
        rideService.acceptRide(id, auth.getName());
        return ResponseEntity.ok("Ride accepted");
    }

    // Complete a ride (driver or user)
    @PostMapping("/rides/{id}/complete")
    public ResponseEntity<String> completeRide(@PathVariable String id) {
        rideService.completeRide(id);
        return ResponseEntity.ok("Ride completed");
    }

    // User: view their own rides
    @GetMapping("/user/rides")
    public ResponseEntity<List<Ride>> getMyRides(Authentication auth) {
        List<Ride> list = rideService.getUserRides(auth.getName());
        return ResponseEntity.ok(list);
    }
}

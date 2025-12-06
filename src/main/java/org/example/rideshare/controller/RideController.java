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

    // Passenger: request a new ride
    @PostMapping("/rides")
    public ResponseEntity<Ride> create(@Valid @RequestBody RideCreateRequest req,
                                       Authentication auth) {
        Ride r = rideService.createRide(auth.getName(), req);
        return ResponseEntity.ok(r);
    }

    // Driver: view all pending rides
    @GetMapping("/driver/rides/requests")
    public ResponseEntity<List<Ride>> pending() {
        return ResponseEntity.ok(rideService.getPendingRides());
    }

    // Driver: accept a ride
    @PostMapping("/driver/rides/{id}/accept")
    public ResponseEntity<String> accept(@PathVariable String id, Authentication auth) {
        rideService.acceptRide(id, auth.getName());
        return ResponseEntity.ok("accepted");
    }

    // User or Driver: complete a ride
    @PostMapping("/rides/{id}/complete")
    public ResponseEntity<String> complete(@PathVariable String id) {
        rideService.completeRide(id);
        return ResponseEntity.ok("done");
    }

    // User: view own rides
    @GetMapping("/user/rides")
    public ResponseEntity<List<Ride>> my(Authentication auth) {
        return ResponseEntity.ok(rideService.getUserRides(auth.getName()));
    }
}

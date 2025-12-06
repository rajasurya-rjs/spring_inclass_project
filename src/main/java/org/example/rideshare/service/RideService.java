package org.example.rideshare.service;

import org.example.rideshare.dto.RideCreateRequest;
import org.example.rideshare.exception.BadRequestException;
import org.example.rideshare.exception.NotFoundException;
import org.example.rideshare.model.Ride;
import org.example.rideshare.repository.RideRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideService {

    private final RideRepository repo;

    public RideService(RideRepository repo) {
        this.repo = repo;
    }

    // Create a new ride for a user
    public Ride createRide(String userId, RideCreateRequest request) {
        Ride r = new Ride();
        r.setUserId(userId);
        r.setPickupLocation(request.getPickupLocation());
        r.setDropLocation(request.getDropLocation());
        // status and createdAt are set by Ride constructor
        return repo.save(r);
    }

    // Get all rides for a user
    public List<Ride> getUserRides(String userId) {
        return repo.findByUserId(userId);
    }

    // Get all pending (requested) rides
    public List<Ride> getPendingRides() {
        return repo.findByStatus("REQUESTED");
    }

    // Accept a ride request (driver assigns themselves)
    public Ride acceptRide(String rideId, String driverId) {
        Ride r = repo.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));
        if (!"REQUESTED".equals(r.getStatus())) {
            throw new BadRequestException("Ride is not in REQUESTED status");
        }
        r.setDriverId(driverId);
        r.setStatus("ACCEPTED");
        return repo.save(r);
    }

    // Complete a ride
    public Ride completeRide(String rideId) {
        Ride r = repo.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));
        if (!"ACCEPTED".equals(r.getStatus())) {
            throw new BadRequestException("Ride is not in ACCEPTED status");
        }
        r.setStatus("COMPLETED");
        return repo.save(r);
    }
}

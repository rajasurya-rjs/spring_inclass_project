package org.example.rideshare.service;

import org.example.rideshare.dto.RideCreateRequest;
import org.example.rideshare.exception.NotFoundException;
import org.example.rideshare.exception.BadRequestException;
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

    public Ride createRide(String userId, RideCreateRequest request) {
        Ride r = new Ride();
        r.setUserId(userId);
        r.setPickupLocation(request.getPickupLocation());
        r.setDropLocation(request.getDropLocation());

        return repo.save(r);
    }

    public List<Ride> getUserRides(String userId) {
        return repo.findByUserId(userId);
    }

    public List<Ride> getPendingRides() {
        return repo.findByStatus("REQUESTED");
    }

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

    public Ride completeRide(String rideId) {
        Ride r = repo.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));

        if (!"ACCEPTED".equals(r.getStatus())) {
            throw new BadRequestException("Ride is not ACCEPTED yet");
        }

        // Simple fare logic (static for now)
        r.setFare(150.0);

        r.setStatus("COMPLETED");
        return repo.save(r);
    }
}

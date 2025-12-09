package org.example.rideshare.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import java.util.Date;

@Document(collection = "rides")
public class Ride {

    @Id
    private String id;

    private String userId;
    private String driverId;

    @NotBlank
    private String pickupLocation;

    @NotBlank
    private String dropLocation;

    private Double fare; // NEW FIELD

    private String status; // REQUESTED, ACCEPTED, COMPLETED
    private Date createdAt;

    public Ride() {
        this.status = "REQUESTED";
        this.createdAt = new Date();
        this.fare = 0.0; // default
    }

    // ======= GETTERS & SETTERS =======

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getDriverId() { return driverId; }
    public void setDriverId(String driverId) { this.driverId = driverId; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDropLocation() { return dropLocation; }
    public void setDropLocation(String dropLocation) { this.dropLocation = dropLocation; }

    public Double getFare() { return fare; }
    public void setFare(Double fare) { this.fare = fare; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}

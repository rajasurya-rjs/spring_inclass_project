package org.example.rideshare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RideCreateRequest {

    @NotBlank(message = "pickup required")
    @Size(min = 3)
    private String pickupLocation;

    @NotBlank(message = "drop required")
    @Size(min = 3)
    private String dropLocation;

    public String getPickupLocation(){ return pickupLocation; }
    public void setPickupLocation(String v){ pickupLocation = v; }

    public String getDropLocation(){ return dropLocation; }
    public void setDropLocation(String v){ dropLocation = v; }
}

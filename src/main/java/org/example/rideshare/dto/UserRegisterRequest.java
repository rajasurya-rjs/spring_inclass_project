package org.example.rideshare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserRegisterRequest {

    @NotBlank(message = "username required")
    private String username;

    @NotBlank(message = "password required")
    private String password;

    @NotBlank(message = "role required")
    @Pattern(regexp = "USER|DRIVER", message = "role must be USER or DRIVER")
    private String role;

    public String getUsername(){ return username; }
    public void setUsername(String v){ username = v; }

    public String getPassword(){ return password; }
    public void setPassword(String v){ password = v; }

    public String getRole(){ return role; }
    public void setRole(String v){ role = v; }
}

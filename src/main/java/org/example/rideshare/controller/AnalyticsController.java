package org.example.rideshare.controller;

import org.example.rideshare.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    // Driver earnings API
    @GetMapping("/driver/earnings")
    public ResponseEntity<Double> earnings(Authentication auth) {
        Double total = analyticsService.totalEarnings(auth.getName());
        return ResponseEntity.ok(total);
    }
}

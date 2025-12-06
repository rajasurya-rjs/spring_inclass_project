package org.example.rideshare.config;

import org.example.rideshare.util.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable());

        http.sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/api/auth/**").permitAll()
                // Driver endpoints: ROLE_DRIVER only
                .requestMatchers("/api/v1/driver/**").hasRole("DRIVER")
                // User endpoints: ROLE_USER only
                .requestMatchers("/api/v1/user/**").hasRole("USER")
                // Ride creation endpoint: only ROLE_USER (passenger) can request a ride
                .requestMatchers("/api/v1/rides").hasRole("USER")
                // Ride completion can be done by any authenticated user (driver or user)
                .requestMatchers("/api/v1/rides/**/complete").authenticated()
                // Any other request must be authenticated
                .anyRequest().authenticated()
        );

        // JWT filter to validate tokens before username/password auth filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // Password encoder bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Expose AuthenticationManager (if needed elsewhere)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}

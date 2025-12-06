package org.example.rideshare.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET = "myverylongsecretkeyformytoken123456";

    public static String generateToken(String username, String role){
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60)) // 1 hr
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
    }

    public static String getUsernameFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(SECRET.getBytes()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public static String getRoleFromToken(String token){
        return (String)Jwts.parserBuilder()
                .setSigningKey(SECRET.getBytes()).build()
                .parseClaimsJws(token).getBody().get("role");
    }
}

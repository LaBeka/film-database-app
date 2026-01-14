package com.edu.film_database.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    private final String secret = "supersecretkey123456789abcdcdcdcdcdcdcdcdcdcd";

    public String generateToken(UserDetails user) {
        Map<String,Object> claims = new HashMap<>();

        claims.put("roles", user
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList());

        String compact = Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 3000)) // 50 hours
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
        return compact;
    }

    public Claims extractAllClaims(String token) {
        Claims body = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return body;
    }
    public String extractUserName(String token){
        return extractAllClaims(token).getSubject();
    }

    public List<String> extractRoles(String token){
        return extractAllClaims(token).get("roles", List.class);
    }
}

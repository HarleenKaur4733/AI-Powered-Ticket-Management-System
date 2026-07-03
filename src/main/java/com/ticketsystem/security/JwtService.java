package com.ticketsystem.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * Creates the signing key using the secret from application.properties
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates JWT for authenticated user
     */
    public String generateToken(UserDetails userDetails) {

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extract username(email) from JWT
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract expiration date from JWT
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Generic method to extract any claim
     */
    public <T> T extractClaim(String token,
            Function<Claims, T> claimsResolver) {

        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parse JWT and return all claims
     */
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Check whether token has expired
     */
    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    /**
     * Validate token
     */
    public boolean isTokenValid(String token,
            UserDetails userDetails) {

        String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

}
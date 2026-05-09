package com.intership.tool.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    private final String SECRET = "thisisaverysecuresecretkeyforjwtgeneration12345";

    // ✅ Generate Token
    public String generateToken(String username, String role) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)   // ✅ correct method
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hr
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    // ✅ Extract Username
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // ✅ Extract Role
    public String extractRole(String token) {
        return (String) extractClaims(token).get("role");
    }

    // ✅ Extract All Claims
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)   // ✅ correct old API
                .parseClaimsJws(token)
                .getBody();
    }
}
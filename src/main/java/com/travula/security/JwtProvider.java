package com.travula.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtProvider {

    private final String key = "securesecuresecuresecuresecuresecuresecuresecuresecuresecuresecuresecuresecuresecuresecuresecure";
    JwtParser jwtParser = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(key.getBytes()))
            .build();
    public String generateToken(Authentication authentication){
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .claim("authorities",principal.getAuthorities())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
                .signWith(Keys.hmacShaKeyFor(key.getBytes()))
                .compact();
    }

    public String generateTokenWithUsername(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(336,ChronoUnit.HOURS)))
                .signWith(Keys.hmacShaKeyFor(key.getBytes()))
                .compact();
    }

    public boolean validateToken(String token) {
        jwtParser.parseClaimsJws(token);
        return true;
    }

    public String getUsernameFromJwt(String token) {
        Claims claims = jwtParser
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}

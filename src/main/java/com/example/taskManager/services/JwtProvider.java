package com.example.taskManager.services;

import com.example.taskManager.datasource.entity.user.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtProvider {
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 7 дней

    @Value("${auth.jwt.secretKey}")
    private String key;
    private SecretKey secret;
    @PostConstruct
    private void init() {
        this.secret = getSignInKey();
    }

    public String generateAccessToken(UserEntity user) {
        Map<String,Object> claims = new HashMap<>();
        String roles = user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
        claims.put("roles", roles);
        claims.put("id",user.getId());
        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(60, ChronoUnit.MINUTES)))
                .signWith(secret)
                .compact();
    }

    public String generateRefreshToken(UserEntity user) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("id",user.getId());
        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(getSignInKey())
                .compact();
    }


    public boolean validateAccessToken(String token,String  username) {
        return isTokenValid(token, username);
    }


    public UUID validateRefreshToken(String token) {
        return UUID.fromString(Jwts.parser()
                .decryptWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject());
    }


    public boolean isTokenValid(String token, String  username) {
        final String userName = getUserName(token);
        return (userName.equals(username) && !isTokenExpired(token));
    }
    public String getUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public List<String> getRoles(String token) {
        Claims claims = extractAllClaims(token);
        Object rolesObj = claims.get("roles");

        if (rolesObj instanceof List<?>) {
            return ((List<?>) rolesObj).stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .toList();
        }

        return Collections.emptyList();
    }
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private SecretKey getSignInKey() {
        byte[] bytes = Base64.getDecoder().decode(key);
        return new SecretKeySpec(bytes, "HmacSHA256");
    }
}


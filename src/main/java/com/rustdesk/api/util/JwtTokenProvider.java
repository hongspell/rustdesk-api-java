package com.rustdesk.api.util;

import com.rustdesk.api.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Token Provider
 * <p>
 * Provides JWT token generation, validation, and extraction functionality.
 * Uses HMAC SHA-256 algorithm for token signing.
 * </p>
 *
 * @author RustDesk
 * @version 2.0.0
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${rustdesk.jwt.secret:}")
    private String jwtSecret;

    @Value("${rustdesk.jwt.expiration:86400000}")
    private long jwtExpirationMs;

    @Value("${rustdesk.jwt.issuer:rustdesk-api}")
    private String jwtIssuer;

    /**
     * Generate JWT token for user
     *
     * @param user user entity
     * @return JWT token string
     * @throws IllegalStateException if JWT secret is not configured
     */
    public String generateToken(User user) {
        if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
            throw new IllegalStateException("JWT secret is not configured");
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("isAdmin", user.getIsAdmin());

        return Jwts.builder()
                .subject(user.getId().toString())
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .issuer(jwtIssuer)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Validate JWT token
     *
     * @param token JWT token to validate
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Get user ID from JWT token
     *
     * @param token JWT token
     * @return user ID
     * @throws JwtException if token is invalid or expired
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        Object userId = claims.get("userId");

        if (userId instanceof Integer) {
            return ((Integer) userId).longValue();
        } else if (userId instanceof Long) {
            return (Long) userId;
        } else {
            // Fallback to subject
            return Long.parseLong(claims.getSubject());
        }
    }

    /**
     * Get username from JWT token
     *
     * @param token JWT token
     * @return username
     * @throws JwtException if token is invalid or expired
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("username", String.class);
    }

    /**
     * Get isAdmin flag from JWT token
     *
     * @param token JWT token
     * @return isAdmin flag
     * @throws JwtException if token is invalid or expired
     */
    public Boolean getIsAdminFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("isAdmin", Boolean.class);
    }

    /**
     * Get expiration date from JWT token
     *
     * @param token JWT token
     * @return expiration date
     * @throws JwtException if token is invalid or expired
     */
    public Date getExpirationFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration();
    }

    /**
     * Check if JWT token is expired
     *
     * @param token JWT token
     * @return true if token is expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getExpirationFromToken(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            log.error("Error checking token expiration: {}", e.getMessage());
            return true;
        }
    }

    /**
     * Check if JWT secret is configured
     *
     * @return true if JWT secret is configured, false otherwise
     */
    public boolean isJwtEnabled() {
        return jwtSecret != null && !jwtSecret.trim().isEmpty();
    }

    /**
     * Get all claims from JWT token
     *
     * @param token JWT token
     * @return claims object
     * @throws JwtException if token is invalid or expired
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Get signing key for JWT token
     *
     * @return secret key for signing
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

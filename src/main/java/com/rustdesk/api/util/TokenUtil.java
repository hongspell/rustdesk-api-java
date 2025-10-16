package com.rustdesk.api.util;

import com.rustdesk.api.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.UUID;

/**
 * Token generation utility
 * <p>
 * Provides token generation methods for authentication.
 * Supports MD5-based tokens (for legacy systems when JWT is disabled)
 * and UUID generation.
 * </p>
 *
 * @author RustDesk
 * @version 2.0.0
 */
@Slf4j
@Component
public class TokenUtil {

    /**
     * Generate MD5 token for user
     * <p>
     * This method generates a token based on user information and current timestamp.
     * Used when JWT secret is not configured or for compatibility with legacy systems.
     * </p>
     *
     * @param user user entity
     * @return MD5 token string
     * @throws RuntimeException if MD5 algorithm is not available
     */
    public static String generateMd5Token(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        try {
            // Create token string from user info and timestamp
            String tokenString = String.format("%d_%s_%d",
                    user.getId(),
                    user.getUsername(),
                    System.currentTimeMillis()
            );

            return md5Hash(tokenString);
        } catch (NoSuchAlgorithmException e) {
            log.error("Failed to generate MD5 token: {}", e.getMessage());
            throw new RuntimeException("Failed to generate token", e);
        }
    }

    /**
     * Generate MD5 token with custom salt
     * <p>
     * This method allows custom salt to be added to the token generation process.
     * </p>
     *
     * @param user user entity
     * @param salt custom salt string
     * @return MD5 token string
     * @throws RuntimeException if MD5 algorithm is not available
     */
    public static String generateMd5Token(User user, String salt) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        try {
            String tokenString = String.format("%d_%s_%s_%d",
                    user.getId(),
                    user.getUsername(),
                    salt,
                    System.currentTimeMillis()
            );

            return md5Hash(tokenString);
        } catch (NoSuchAlgorithmException e) {
            log.error("Failed to generate MD5 token with salt: {}", e.getMessage());
            throw new RuntimeException("Failed to generate token", e);
        }
    }

    /**
     * Generate UUID token
     * <p>
     * Generates a random UUID that can be used as a token.
     * Returns UUID without hyphens.
     * </p>
     *
     * @return UUID string without hyphens
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Generate UUID token with hyphens
     *
     * @return UUID string with hyphens
     */
    public static String generateUuidWithHyphens() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generate MD5 hash of a string
     *
     * @param input input string to hash
     * @return MD5 hash in hexadecimal format
     * @throws NoSuchAlgorithmException if MD5 algorithm is not available
     */
    private static String md5Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(input.getBytes());
        return HexFormat.of().formatHex(digest);
    }

    /**
     * Generate secure random token
     * <p>
     * Generates a secure random token using UUID and MD5 hashing.
     * More secure than simple UUID generation.
     * </p>
     *
     * @return secure random token string
     */
    public static String generateSecureToken() {
        try {
            String uuid = UUID.randomUUID().toString();
            String timestamp = String.valueOf(System.currentTimeMillis());
            String combined = uuid + timestamp;
            return md5Hash(combined);
        } catch (NoSuchAlgorithmException e) {
            log.error("Failed to generate secure token: {}", e.getMessage());
            // Fallback to UUID if MD5 fails
            return generateUuid();
        }
    }

    /**
     * Validate token format
     * <p>
     * Checks if token is a valid MD5 hash (32 character hex string) or UUID format.
     * </p>
     *
     * @param token token to validate
     * @return true if token format is valid
     */
    public static boolean isValidTokenFormat(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        // Check MD5 format (32 hex characters)
        if (token.length() == 32 && token.matches("^[a-fA-F0-9]{32}$")) {
            return true;
        }

        // Check UUID format without hyphens (32 hex characters)
        if (token.length() == 32 && token.matches("^[a-fA-F0-9]{32}$")) {
            return true;
        }

        // Check UUID format with hyphens
        if (token.matches("^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$")) {
            return true;
        }

        return false;
    }
}

package com.rustdesk.api.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Password encryption and verification utility
 * <p>
 * Provides password encryption using BCrypt and supports automatic upgrade
 * from legacy MD5 passwords to BCrypt.
 * </p>
 *
 * @author RustDesk
 * @version 2.0.0
 */
@Slf4j
@Component
public class PasswordUtil {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final int MD5_HEX_LENGTH = 32;

    /**
     * Encrypt password using BCrypt algorithm
     *
     * @param password raw password to encrypt
     * @return BCrypt encrypted password
     * @throws IllegalArgumentException if password is null or empty
     */
    public static String encryptPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return passwordEncoder.encode(password);
    }

    /**
     * Verify password against encoded password
     * <p>
     * Supports both BCrypt and legacy MD5 passwords. If a MD5 password is detected
     * (32 character hex string), it will be verified using MD5 algorithm.
     * </p>
     *
     * @param rawPassword     raw password to verify
     * @param encodedPassword encoded password from database
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }

        // Check if it's a legacy MD5 password (32 character hex string)
        if (isLegacyMd5Password(encodedPassword)) {
            log.debug("Detected legacy MD5 password, verifying with MD5");
            return verifyMd5Password(rawPassword, encodedPassword);
        }

        // Verify using BCrypt
        try {
            return passwordEncoder.matches(rawPassword, encodedPassword);
        } catch (Exception e) {
            log.error("Error verifying password: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Check if a password needs to be upgraded from MD5 to BCrypt
     * <p>
     * A password needs upgrade if it's a 32-character hex string (MD5 format).
     * After successful login with MD5 password, the application should upgrade
     * the password to BCrypt for better security.
     * </p>
     *
     * @param encodedPassword encoded password from database
     * @return true if password needs upgrade, false otherwise
     */
    public static boolean needsUpgrade(String encodedPassword) {
        return isLegacyMd5Password(encodedPassword);
    }

    /**
     * Check if encoded password is a legacy MD5 password
     *
     * @param encodedPassword encoded password to check
     * @return true if it's MD5 format (32 character hex string)
     */
    private static boolean isLegacyMd5Password(String encodedPassword) {
        if (encodedPassword == null || encodedPassword.length() != MD5_HEX_LENGTH) {
            return false;
        }
        // Check if string contains only hexadecimal characters
        return encodedPassword.matches("^[a-fA-F0-9]{32}$");
    }

    /**
     * Verify password using MD5 algorithm (for legacy passwords)
     *
     * @param rawPassword raw password to verify
     * @param md5Password MD5 hashed password
     * @return true if password matches
     */
    private static boolean verifyMd5Password(String rawPassword, String md5Password) {
        try {
            String hashed = md5Hash(rawPassword);
            return hashed.equalsIgnoreCase(md5Password);
        } catch (Exception e) {
            log.error("Error verifying MD5 password: {}", e.getMessage());
            return false;
        }
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
     * Generate random salt for password encryption (for legacy systems)
     * <p>
     * Note: BCrypt automatically generates salt, this method is kept for
     * compatibility with legacy systems.
     * </p>
     *
     * @param length length of salt to generate
     * @return random salt string
     * @deprecated Use BCrypt which handles salt automatically
     */
    @Deprecated
    public static String generateSalt(int length) {
        return java.util.UUID.randomUUID().toString().substring(0, length);
    }
}

package com.rustdesk.api.service;

import com.rustdesk.api.config.properties.RustDeskProperties;
import com.rustdesk.api.entity.User;
import com.rustdesk.api.entity.UserToken;
import com.rustdesk.api.repository.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * User Token Service
 * Manages user authentication tokens including creation, validation, refresh, and cleanup.
 *
 * @author RustDesk API Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserTokenService {

    private final UserTokenRepository userTokenRepository;
    private final RustDeskProperties rustDeskProperties;

    /**
     * Create a new token for user
     * Generates a unique token and stores it with the associated device information.
     *
     * @param user user entity
     * @param deviceId device ID
     * @param deviceUuid device UUID
     * @return created user token
     */
    @Transactional
    public UserToken createToken(User user, String deviceId, String deviceUuid) {
        log.info("Creating token for user: {} with deviceId: {}", user.getUsername(), deviceId);

        UserToken userToken = new UserToken();
        userToken.setUserId(user.getId());
        userToken.setDeviceId(deviceId);
        userToken.setDeviceUuid(deviceUuid);

        // Generate unique token
        String token = generateToken();
        userToken.setToken(token);

        // Set expiration time based on configuration
        Long expirationSeconds = rustDeskProperties.getToken().getExpiration();
        Long expiredAt = System.currentTimeMillis() + expirationSeconds * 1000;
        userToken.setExpiredAt(expiredAt);

        UserToken savedToken = userTokenRepository.save(userToken);
        log.info("Token created successfully for user: {}, expires at: {}", user.getUsername(), expiredAt);
        return savedToken;
    }

    /**
     * Find token by token string
     *
     * @param token token string
     * @return Optional UserToken
     */
    @Transactional(readOnly = true)
    public Optional<UserToken> findByToken(String token) {
        log.debug("Finding token: {}", token);
        return userTokenRepository.findByToken(token);
    }

    /**
     * Validate token
     * Checks if the token exists and is not expired.
     *
     * @param token token string
     * @return true if token is valid, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean validateToken(String token) {
        log.debug("Validating token: {}", token);

        Optional<UserToken> userTokenOpt = findByToken(token);
        if (userTokenOpt.isEmpty()) {
            log.debug("Token not found: {}", token);
            return false;
        }

        UserToken userToken = userTokenOpt.get();

        // Check if token is expired
        if (userToken.getExpiredAt() < System.currentTimeMillis()) {
            log.debug("Token expired: {}", token);
            return false;
        }

        log.debug("Token is valid: {}", token);
        return true;
    }

    /**
     * Delete all tokens for a user (logout)
     * Removes all active sessions for the specified user.
     *
     * @param userId user ID
     */
    @Transactional
    public void deleteByUserId(Long userId) {
        log.info("Deleting all tokens for user: {}", userId);
        userTokenRepository.deleteByUserId(userId);
        log.info("All tokens deleted for user: {}", userId);
    }

    /**
     * Auto refresh token if it's close to expiration
     * If the token is valid but will expire soon, extend its expiration time.
     *
     * @param token user token entity
     * @return updated token if refreshed, original token otherwise
     */
    @Transactional
    public UserToken autoRefreshToken(UserToken token) {
        log.debug("Checking if token needs refresh: {}", token.getToken());

        Long now = System.currentTimeMillis();
        Long expiredAt = token.getExpiredAt();

        // If token expires within 24 hours, refresh it
        if (expiredAt < now + 24 * 60 * 60 * 1000) {
            log.info("Refreshing token: {}", token.getToken());

            Long expirationSeconds = rustDeskProperties.getToken().getExpiration();
            Long newExpiredAt = now + expirationSeconds * 1000;
            token.setExpiredAt(newExpiredAt);

            UserToken updatedToken = userTokenRepository.save(token);
            log.info("Token refreshed, new expiration: {}", newExpiredAt);
            return updatedToken;
        }

        log.debug("Token does not need refresh: {}", token.getToken());
        return token;
    }

    /**
     * Clean expired tokens
     * Scheduled task that runs periodically to remove expired tokens from the database.
     * Runs every day at 2 AM.
     */
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void cleanExpiredTokens() {
        log.info("Starting scheduled cleanup of expired tokens");

        try {
            Long now = System.currentTimeMillis();
            int deletedCount = 0;

            // Find and delete expired tokens
            // Note: The repository method signature might need adjustment based on actual implementation
            // This is a simplified version - you may want to batch delete for better performance
            var allTokens = userTokenRepository.findAll();
            for (UserToken token : allTokens) {
                if (token.getExpiredAt() < now) {
                    userTokenRepository.delete(token);
                    deletedCount++;
                }
            }

            log.info("Expired tokens cleanup completed. Deleted {} tokens", deletedCount);
        } catch (Exception e) {
            log.error("Error during expired tokens cleanup", e);
        }
    }

    /**
     * Generate a unique token string
     *
     * @return unique token string
     */
    private String generateToken() {
        return UUID.randomUUID().toString().replace("-", "") +
               System.currentTimeMillis() +
               UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Create token with default device info
     * Convenience method when device information is not available.
     *
     * @param user user entity
     * @return created user token
     */
    @Transactional
    public UserToken createToken(User user) {
        return createToken(user, null, null);
    }

    /**
     * Delete specific token (single session logout)
     *
     * @param token token string
     */
    @Transactional
    public void deleteToken(String token) {
        log.info("Deleting token: {}", token);
        Optional<UserToken> userTokenOpt = findByToken(token);
        userTokenOpt.ifPresent(userToken -> {
            userTokenRepository.delete(userToken);
            log.info("Token deleted successfully");
        });
    }
}

package com.rustdesk.api.service;

import com.rustdesk.api.entity.User;
import com.rustdesk.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * User Service
 * Provides business logic for user management including authentication, registration, and profile updates.
 *
 * @author RustDesk API Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Find user by ID
     *
     * @param id user ID
     * @return Optional User
     */
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        log.debug("Finding user by id: {}", id);
        return userRepository.findById(id);
    }

    /**
     * Find user by username
     *
     * @param username username
     * @return Optional User
     */
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        log.debug("Finding user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    /**
     * Find user by email
     *
     * @param email email address
     * @return Optional User
     */
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    /**
     * Create a new user
     * Validates that username is unique and encrypts the password before saving.
     *
     * @param user user entity to create
     * @return created user
     * @throws IllegalArgumentException if username already exists
     */
    @Transactional
    public User createUser(User user) {
        log.info("Creating new user: {}", user.getUsername());

        // Check if username already exists
        if (existsByUsername(user.getUsername())) {
            log.warn("Username already exists: {}", user.getUsername());
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }

        // Check if email already exists (if email is provided)
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            if (userRepository.existsByEmail(user.getEmail())) {
                log.warn("Email already exists: {}", user.getEmail());
                throw new IllegalArgumentException("Email already exists: " + user.getEmail());
            }
        }

        // Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default values if not provided
        if (user.getIsAdmin() == null) {
            user.setIsAdmin(false);
        }
        if (user.getStatus() == null) {
            user.setStatus(1);
        }

        User savedUser = userRepository.save(user);
        log.info("User created successfully with id: {}", savedUser.getId());
        return savedUser;
    }

    /**
     * Update an existing user
     * Note: This method does not update the password. Use changePassword() for password updates.
     *
     * @param user user entity with updated information
     * @return updated user
     * @throws IllegalArgumentException if user not found
     */
    @Transactional
    public User updateUser(User user) {
        log.info("Updating user: {}", user.getId());

        if (!userRepository.existsById(user.getId())) {
            log.warn("User not found with id: {}", user.getId());
            throw new IllegalArgumentException("User not found with id: " + user.getId());
        }

        User savedUser = userRepository.save(user);
        log.info("User updated successfully: {}", savedUser.getId());
        return savedUser;
    }

    /**
     * Delete user by ID
     * This is a hard delete operation. Consider implementing soft delete if needed.
     *
     * @param id user ID to delete
     * @throws IllegalArgumentException if user not found
     */
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);

        if (!userRepository.existsById(id)) {
            log.warn("User not found with id: {}", id);
            throw new IllegalArgumentException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
        log.info("User deleted successfully: {}", id);
    }

    /**
     * Authenticate user with username and password
     * Verifies the username and password, returning the user if authentication succeeds.
     *
     * @param username username
     * @param password raw password (not encoded)
     * @return Optional User if authentication succeeds, empty otherwise
     */
    @Transactional(readOnly = true)
    public Optional<User> authenticate(String username, String password) {
        log.debug("Authenticating user: {}", username);

        Optional<User> userOpt = findByUsername(username);
        if (userOpt.isEmpty()) {
            log.debug("User not found: {}", username);
            return Optional.empty();
        }

        User user = userOpt.get();

        // Check if user is active
        if (user.getStatus() == null || user.getStatus() != 1) {
            log.warn("User is not active: {}", username);
            return Optional.empty();
        }

        // Verify password
        if (passwordEncoder.matches(password, user.getPassword())) {
            log.info("User authenticated successfully: {}", username);
            return Optional.of(user);
        }

        log.debug("Invalid password for user: {}", username);
        return Optional.empty();
    }

    /**
     * Change user password
     * Verifies the old password before setting the new one.
     *
     * @param userId user ID
     * @param oldPassword current password
     * @param newPassword new password
     * @return true if password changed successfully, false otherwise
     */
    @Transactional
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        log.info("Changing password for user: {}", userId);

        Optional<User> userOpt = findById(userId);
        if (userOpt.isEmpty()) {
            log.warn("User not found: {}", userId);
            return false;
        }

        User user = userOpt.get();

        // Verify old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            log.warn("Old password does not match for user: {}", userId);
            return false;
        }

        // Encode and set new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        log.info("Password changed successfully for user: {}", userId);
        return true;
    }

    /**
     * Check if user is an administrator
     *
     * @param user user entity
     * @return true if user is admin, false otherwise
     */
    public boolean isAdmin(User user) {
        return user != null && Boolean.TRUE.equals(user.getIsAdmin());
    }

    /**
     * Find all users
     *
     * @return list of all users
     */
    @Transactional(readOnly = true)
    public java.util.List<User> findAll() {
        log.debug("Finding all users");
        return userRepository.findAll();
    }

    /**
     * Check if username exists
     *
     * @param username username to check
     * @return true if username exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}

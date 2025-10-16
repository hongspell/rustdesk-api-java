package com.rustdesk.api.repository;

import com.rustdesk.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * User repository interface
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by username
     *
     * @param username username
     * @return Optional User
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email
     *
     * @param email email
     * @return Optional User
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if username exists
     *
     * @param username username
     * @return true if exists
     */
    boolean existsByUsername(String username);

    /**
     * Check if email exists
     *
     * @param email email
     * @return true if exists
     */
    boolean existsByEmail(String email);

    /**
     * Find users by group ID
     *
     * @param groupId group ID
     * @return List of users
     */
    List<User> findByGroupId(Long groupId);
}

package com.rustdesk.api.repository;

import com.rustdesk.api.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * UserToken repository interface
 */
@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    /**
     * Find user token by token
     *
     * @param token token string
     * @return Optional UserToken
     */
    Optional<UserToken> findByToken(String token);

    /**
     * Find user tokens by user ID
     *
     * @param userId user ID
     * @return List of user tokens
     */
    List<UserToken> findByUserId(Long userId);

    /**
     * Delete user tokens by user ID
     *
     * @param userId user ID
     */
    void deleteByUserId(Long userId);

    /**
     * Delete expired tokens
     *
     * @param expiredAt expired timestamp
     */
    void deleteByExpiredAtBefore(Long expiredAt);
}

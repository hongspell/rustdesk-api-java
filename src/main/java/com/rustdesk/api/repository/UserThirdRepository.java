package com.rustdesk.api.repository;

import com.rustdesk.api.entity.UserThird;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * UserThird repository interface
 */
@Repository
public interface UserThirdRepository extends JpaRepository<UserThird, Long> {

    /**
     * Find user third-party binding by user ID and operation
     *
     * @param userId user ID
     * @param op operation name
     * @return Optional UserThird
     */
    Optional<UserThird> findByUserIdAndOp(Long userId, String op);

    /**
     * Find user third-party binding by openID and operation
     *
     * @param openId open ID
     * @param op operation name
     * @return Optional UserThird
     */
    Optional<UserThird> findByOpenIdAndOp(String openId, String op);

    /**
     * Find user third-party bindings by user ID
     *
     * @param userId user ID
     * @return List of user third-party bindings
     */
    List<UserThird> findByUserId(Long userId);
}

package com.rustdesk.api.repository;

import com.rustdesk.api.entity.LoginLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * LoginLog repository interface
 */
@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {

    /**
     * Find login logs by user ID
     *
     * @param userId user ID
     * @return List of login logs
     */
    List<LoginLog> findByUserId(Long userId);

    /**
     * Find login logs by deleted status with pagination
     *
     * @param isDeleted deleted status (0: not deleted, 1: deleted)
     * @param pageable pagination parameters
     * @return Page of login logs
     */
    Page<LoginLog> findByIsDeleted(Integer isDeleted, Pageable pageable);
}

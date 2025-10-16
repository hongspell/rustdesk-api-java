package com.rustdesk.api.repository;

import com.rustdesk.api.entity.ShareRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ShareRecord repository interface
 */
@Repository
public interface ShareRecordRepository extends JpaRepository<ShareRecord, Long> {

    /**
     * Find share record by share token
     *
     * @param shareToken share token
     * @return Optional ShareRecord
     */
    Optional<ShareRecord> findByShareToken(String shareToken);

    /**
     * Find share records by user ID
     *
     * @param userId user ID
     * @return List of share records
     */
    List<ShareRecord> findByUserId(Long userId);

    /**
     * Delete expired share records
     *
     * @param expire expiration timestamp
     */
    void deleteByExpireBefore(Long expire);
}

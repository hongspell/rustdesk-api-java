package com.rustdesk.api.repository;

import com.rustdesk.api.entity.AuditFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * AuditFile repository interface
 */
@Repository
public interface AuditFileRepository extends JpaRepository<AuditFile, Long> {

    /**
     * Find file audit records by peer ID
     *
     * @param peerId peer ID
     * @return List of file audit records
     */
    List<AuditFile> findByPeerId(String peerId);

    /**
     * Find all file audit records with pagination
     *
     * @param pageable pagination parameters
     * @return Page of file audit records
     */
    Page<AuditFile> findAll(Pageable pageable);
}

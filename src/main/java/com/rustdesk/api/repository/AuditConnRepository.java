package com.rustdesk.api.repository;

import com.rustdesk.api.entity.AuditConn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * AuditConn repository interface
 */
@Repository
public interface AuditConnRepository extends JpaRepository<AuditConn, Long> {

    /**
     * Find connection audit records by peer ID
     *
     * @param peerId peer ID
     * @return List of connection audit records
     */
    List<AuditConn> findByPeerId(String peerId);

    /**
     * Find all connection audit records with pagination
     *
     * @param pageable pagination parameters
     * @return Page of connection audit records
     */
    Page<AuditConn> findAll(Pageable pageable);
}

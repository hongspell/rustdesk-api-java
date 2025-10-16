package com.rustdesk.api.repository;

import com.rustdesk.api.entity.Peer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Peer repository interface
 */
@Repository
public interface PeerRepository extends JpaRepository<Peer, Long> {

    /**
     * Find peer by device ID
     *
     * @param deviceId device ID
     * @return Optional Peer
     */
    Optional<Peer> findByDeviceId(String deviceId);

    /**
     * Find peer by UUID
     *
     * @param uuid UUID
     * @return Optional Peer
     */
    Optional<Peer> findByUuid(String uuid);

    /**
     * Find peers by user ID
     *
     * @param userId user ID
     * @return List of peers
     */
    List<Peer> findByUserId(Long userId);

    /**
     * Find peers by group ID
     *
     * @param groupId group ID
     * @return List of peers
     */
    List<Peer> findByGroupId(Long groupId);
}

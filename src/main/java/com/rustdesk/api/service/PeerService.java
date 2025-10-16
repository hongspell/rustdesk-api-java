package com.rustdesk.api.service;

import com.rustdesk.api.entity.Peer;
import com.rustdesk.api.repository.PeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Peer Service
 * Manages peer (device) information including registration, binding, and status updates.
 *
 * @author RustDesk API Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PeerService {

    private final PeerRepository peerRepository;

    /**
     * Find peer by device ID
     *
     * @param deviceId device ID
     * @return Optional Peer
     */
    @Transactional(readOnly = true)
    public Optional<Peer> findByDeviceId(String deviceId) {
        log.debug("Finding peer by deviceId: {}", deviceId);
        return peerRepository.findByDeviceId(deviceId);
    }

    /**
     * Find all peers for a user
     *
     * @param userId user ID
     * @return list of peers
     */
    @Transactional(readOnly = true)
    public List<Peer> findByUserId(Long userId) {
        log.debug("Finding peers for user: {}", userId);
        return peerRepository.findByUserId(userId);
    }

    /**
     * Save a peer
     * Simple save operation that delegates to the repository.
     *
     * @param peer peer entity
     * @return saved peer
     */
    @Transactional
    public Peer save(Peer peer) {
        log.debug("Saving peer: {}", peer.getDeviceId());
        return peerRepository.save(peer);
    }

    /**
     * Create or update a peer
     * If a peer with the same deviceId exists, it updates the existing peer.
     * Otherwise, it creates a new peer.
     *
     * @param peer peer entity
     * @return saved peer
     */
    @Transactional
    public Peer createOrUpdatePeer(Peer peer) {
        log.info("Creating or updating peer with deviceId: {}", peer.getDeviceId());

        Optional<Peer> existingOpt = peerRepository.findByDeviceId(peer.getDeviceId());

        if (existingOpt.isPresent()) {
            // Update existing peer
            Peer existing = existingOpt.get();
            log.debug("Peer already exists, updating: {}", peer.getDeviceId());

            // Update fields
            if (peer.getCpu() != null) {
                existing.setCpu(peer.getCpu());
            }
            if (peer.getHostname() != null) {
                existing.setHostname(peer.getHostname());
            }
            if (peer.getMemory() != null) {
                existing.setMemory(peer.getMemory());
            }
            if (peer.getOs() != null) {
                existing.setOs(peer.getOs());
            }
            if (peer.getUsername() != null) {
                existing.setUsername(peer.getUsername());
            }
            if (peer.getUuid() != null) {
                existing.setUuid(peer.getUuid());
            }
            if (peer.getVersion() != null) {
                existing.setVersion(peer.getVersion());
            }
            if (peer.getAlias() != null) {
                existing.setAlias(peer.getAlias());
            }
            if (peer.getGroupId() != null) {
                existing.setGroupId(peer.getGroupId());
            }

            Peer saved = peerRepository.save(existing);
            log.info("Peer updated: {}", saved.getDeviceId());
            return saved;
        } else {
            // Create new peer
            log.debug("Creating new peer: {}", peer.getDeviceId());
            Peer saved = peerRepository.save(peer);
            log.info("Peer created with id: {}", saved.getId());
            return saved;
        }
    }

    /**
     * Bind a peer to a user
     * Associates a device with a user account.
     *
     * @param deviceId device ID
     * @param userId user ID
     * @return updated peer
     * @throws IllegalArgumentException if peer not found
     */
    @Transactional
    public Peer bindUser(String deviceId, Long userId) {
        log.info("Binding peer {} to user {}", deviceId, userId);

        Optional<Peer> peerOpt = peerRepository.findByDeviceId(deviceId);
        if (peerOpt.isEmpty()) {
            log.warn("Peer not found: {}", deviceId);
            throw new IllegalArgumentException("Peer not found with deviceId: " + deviceId);
        }

        Peer peer = peerOpt.get();
        peer.setUserId(userId);

        Peer saved = peerRepository.save(peer);
        log.info("Peer bound to user successfully: {} -> {}", deviceId, userId);
        return saved;
    }

    /**
     * Unbind a peer from user
     * Removes the association between a device and user.
     *
     * @param uuid peer UUID
     * @return updated peer
     * @throws IllegalArgumentException if peer not found
     */
    @Transactional
    public Peer unbindUser(String uuid) {
        log.info("Unbinding peer with uuid: {}", uuid);

        Optional<Peer> peerOpt = peerRepository.findByUuid(uuid);
        if (peerOpt.isEmpty()) {
            log.warn("Peer not found with uuid: {}", uuid);
            throw new IllegalArgumentException("Peer not found with uuid: " + uuid);
        }

        Peer peer = peerOpt.get();
        Long previousUserId = peer.getUserId();
        peer.setUserId(null);

        Peer saved = peerRepository.save(peer);
        log.info("Peer unbound from user successfully: {} (was user {})", uuid, previousUserId);
        return saved;
    }

    /**
     * Update peer online status
     * Updates the last online time and IP address for a peer.
     *
     * @param deviceId device ID
     * @param ip IP address
     * @return updated peer
     */
    @Transactional
    public Peer updateOnlineStatus(String deviceId, String ip) {
        log.debug("Updating online status for peer: {}", deviceId);

        Optional<Peer> peerOpt = peerRepository.findByDeviceId(deviceId);
        if (peerOpt.isEmpty()) {
            log.warn("Peer not found: {}", deviceId);
            throw new IllegalArgumentException("Peer not found with deviceId: " + deviceId);
        }

        Peer peer = peerOpt.get();
        peer.setLastOnlineTime(System.currentTimeMillis());
        peer.setLastOnlineIp(ip);

        Peer saved = peerRepository.save(peer);
        log.debug("Online status updated for peer: {}", deviceId);
        return saved;
    }

    /**
     * Find peer by UUID
     *
     * @param uuid peer UUID
     * @return Optional Peer
     */
    @Transactional(readOnly = true)
    public Optional<Peer> findByUuid(String uuid) {
        log.debug("Finding peer by uuid: {}", uuid);
        return peerRepository.findByUuid(uuid);
    }

    /**
     * Find peers by group ID
     *
     * @param groupId group ID
     * @return list of peers
     */
    @Transactional(readOnly = true)
    public List<Peer> findByGroupId(Long groupId) {
        log.debug("Finding peers for group: {}", groupId);
        return peerRepository.findByGroupId(groupId);
    }

    /**
     * Delete a peer
     *
     * @param id peer ID
     */
    @Transactional
    public void deletePeer(Long id) {
        log.info("Deleting peer with id: {}", id);

        if (!peerRepository.existsById(id)) {
            log.warn("Peer not found: {}", id);
            throw new IllegalArgumentException("Peer not found with id: " + id);
        }

        peerRepository.deleteById(id);
        log.info("Peer deleted: {}", id);
    }

    /**
     * Delete peer by device ID
     *
     * @param deviceId device ID
     */
    @Transactional
    public void deleteByDeviceId(String deviceId) {
        log.info("Deleting peer with deviceId: {}", deviceId);

        Optional<Peer> peerOpt = peerRepository.findByDeviceId(deviceId);
        if (peerOpt.isEmpty()) {
            log.warn("Peer not found: {}", deviceId);
            throw new IllegalArgumentException("Peer not found with deviceId: " + deviceId);
        }

        peerRepository.delete(peerOpt.get());
        log.info("Peer deleted: {}", deviceId);
    }

    /**
     * Update peer group
     *
     * @param deviceId device ID
     * @param groupId group ID
     * @return updated peer
     */
    @Transactional
    public Peer updateGroup(String deviceId, Long groupId) {
        log.info("Updating group for peer {} to group {}", deviceId, groupId);

        Optional<Peer> peerOpt = peerRepository.findByDeviceId(deviceId);
        if (peerOpt.isEmpty()) {
            log.warn("Peer not found: {}", deviceId);
            throw new IllegalArgumentException("Peer not found with deviceId: " + deviceId);
        }

        Peer peer = peerOpt.get();
        peer.setGroupId(groupId);

        Peer saved = peerRepository.save(peer);
        log.info("Peer group updated successfully");
        return saved;
    }
}

package com.rustdesk.api.service;

import com.rustdesk.api.entity.AddressBook;
import com.rustdesk.api.entity.Peer;
import com.rustdesk.api.repository.AddressBookRepository;
import com.rustdesk.api.repository.PeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Address Book Service
 * Manages user address books including peers, collections, and tags.
 *
 * @author RustDesk API Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AddressBookService {

    private final AddressBookRepository addressBookRepository;
    private final PeerRepository peerRepository;

    /**
     * Find all address book entries for a user
     *
     * @param userId user ID
     * @return list of address book entries
     */
    @Transactional(readOnly = true)
    public List<AddressBook> findByUserId(Long userId) {
        log.debug("Finding address books for user: {}", userId);
        return addressBookRepository.findByUserId(userId);
    }

    /**
     * Find address book entries by user ID and collection ID
     *
     * @param userId user ID
     * @param collectionId collection ID
     * @return list of address book entries
     */
    @Transactional(readOnly = true)
    public List<AddressBook> findByUserIdAndCollectionId(Long userId, Long collectionId) {
        log.debug("Finding address books for user: {} and collection: {}", userId, collectionId);
        return addressBookRepository.findByUserIdAndCollectionId(userId, collectionId);
    }

    /**
     * Create a new address book entry
     *
     * @param addressBook address book entity
     * @return created address book
     */
    @Transactional
    public AddressBook createAddressBook(AddressBook addressBook) {
        log.info("Creating address book entry for user: {} with deviceId: {}",
                addressBook.getUserId(), addressBook.getDeviceId());

        // Check if entry already exists
        Optional<AddressBook> existingOpt = addressBookRepository.findByUserIdAndDeviceId(
                addressBook.getUserId(), addressBook.getDeviceId());

        if (existingOpt.isPresent()) {
            log.warn("Address book entry already exists for user: {} and device: {}",
                    addressBook.getUserId(), addressBook.getDeviceId());
            throw new IllegalArgumentException("Address book entry already exists for this device");
        }

        AddressBook saved = addressBookRepository.save(addressBook);
        log.info("Address book entry created with id: {}", saved.getId());
        return saved;
    }

    /**
     * Update an existing address book entry
     *
     * @param addressBook address book entity with updated information
     * @return updated address book
     */
    @Transactional
    public AddressBook updateAddressBook(AddressBook addressBook) {
        log.info("Updating address book entry: {}", addressBook.getId());

        if (!addressBookRepository.existsById(addressBook.getId())) {
            log.warn("Address book entry not found: {}", addressBook.getId());
            throw new IllegalArgumentException("Address book entry not found with id: " + addressBook.getId());
        }

        AddressBook saved = addressBookRepository.save(addressBook);
        log.info("Address book entry updated: {}", saved.getId());
        return saved;
    }

    /**
     * Delete an address book entry
     *
     * @param id address book entry ID
     */
    @Transactional
    public void deleteAddressBook(Long id) {
        log.info("Deleting address book entry: {}", id);

        if (!addressBookRepository.existsById(id)) {
            log.warn("Address book entry not found: {}", id);
            throw new IllegalArgumentException("Address book entry not found with id: " + id);
        }

        addressBookRepository.deleteById(id);
        log.info("Address book entry deleted: {}", id);
    }

    /**
     * Batch create address book entries from peer IDs
     * Creates address book entries for the specified user from a list of peer IDs.
     *
     * @param userId user ID
     * @param peerIds list of peer IDs
     * @return list of created address book entries
     */
    @Transactional
    public List<AddressBook> batchCreateFromPeers(Long userId, List<Long> peerIds) {
        log.info("Batch creating address book entries for user: {} from {} peers", userId, peerIds.size());

        List<AddressBook> createdEntries = new ArrayList<>();

        for (Long peerId : peerIds) {
            Optional<Peer> peerOpt = peerRepository.findById(peerId);
            if (peerOpt.isEmpty()) {
                log.warn("Peer not found: {}", peerId);
                continue;
            }

            Peer peer = peerOpt.get();

            // Check if address book entry already exists
            Optional<AddressBook> existingOpt = addressBookRepository.findByUserIdAndDeviceId(userId, peer.getDeviceId());
            if (existingOpt.isPresent()) {
                log.debug("Address book entry already exists for device: {}", peer.getDeviceId());
                continue;
            }

            // Create new address book entry from peer
            AddressBook addressBook = new AddressBook();
            addressBook.setUserId(userId);
            addressBook.setDeviceId(peer.getDeviceId());
            addressBook.setHostname(peer.getHostname());
            addressBook.setAlias(peer.getAlias());
            addressBook.setPlatform(peer.getOs());
            addressBook.setUsername(peer.getUsername());

            AddressBook saved = addressBookRepository.save(addressBook);
            createdEntries.add(saved);
        }

        log.info("Batch created {} address book entries for user: {}", createdEntries.size(), userId);
        return createdEntries;
    }

    /**
     * Batch update tags for address book entries
     * Updates tags for multiple address book entries at once.
     *
     * @param userId user ID (for validation)
     * @param addressBooks list of address book entries with updated tags
     * @return list of updated address book entries
     */
    @Transactional
    public List<AddressBook> updateTags(Long userId, List<AddressBook> addressBooks) {
        log.info("Batch updating tags for {} address book entries for user: {}", addressBooks.size(), userId);

        List<AddressBook> updatedEntries = new ArrayList<>();

        for (AddressBook addressBook : addressBooks) {
            // Verify ownership
            if (!addressBook.getUserId().equals(userId)) {
                log.warn("Address book entry {} does not belong to user {}", addressBook.getId(), userId);
                continue;
            }

            Optional<AddressBook> existingOpt = addressBookRepository.findById(addressBook.getId());
            if (existingOpt.isEmpty()) {
                log.warn("Address book entry not found: {}", addressBook.getId());
                continue;
            }

            AddressBook existing = existingOpt.get();
            existing.setTags(addressBook.getTags());

            AddressBook saved = addressBookRepository.save(existing);
            updatedEntries.add(saved);
        }

        log.info("Updated tags for {} address book entries", updatedEntries.size());
        return updatedEntries;
    }

    /**
     * Find address book entry by user ID and device ID
     *
     * @param userId user ID
     * @param deviceId device ID
     * @return Optional AddressBook
     */
    @Transactional(readOnly = true)
    public Optional<AddressBook> findByUserIdAndDeviceId(Long userId, String deviceId) {
        log.debug("Finding address book for user: {} and device: {}", userId, deviceId);
        return addressBookRepository.findByUserIdAndDeviceId(userId, deviceId);
    }

    /**
     * Delete all address book entries for a user
     *
     * @param userId user ID
     */
    @Transactional
    public void deleteByUserId(Long userId) {
        log.info("Deleting all address book entries for user: {}", userId);
        List<AddressBook> addressBooks = findByUserId(userId);
        addressBookRepository.deleteAll(addressBooks);
        log.info("Deleted {} address book entries for user: {}", addressBooks.size(), userId);
    }
}

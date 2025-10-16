package com.rustdesk.api.repository;

import com.rustdesk.api.entity.AddressBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * AddressBook repository interface
 */
@Repository
public interface AddressBookRepository extends JpaRepository<AddressBook, Long> {

    /**
     * Find address books by user ID
     *
     * @param userId user ID
     * @return List of address books
     */
    List<AddressBook> findByUserId(Long userId);

    /**
     * Find address books by user ID and collection ID
     *
     * @param userId user ID
     * @param collectionId collection ID
     * @return List of address books
     */
    List<AddressBook> findByUserIdAndCollectionId(Long userId, Long collectionId);

    /**
     * Find address book by user ID and device ID
     *
     * @param userId user ID
     * @param deviceId device ID
     * @return Optional AddressBook
     */
    Optional<AddressBook> findByUserIdAndDeviceId(Long userId, String deviceId);
}

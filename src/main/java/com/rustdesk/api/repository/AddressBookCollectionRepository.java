package com.rustdesk.api.repository;

import com.rustdesk.api.entity.AddressBookCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * AddressBookCollection repository interface
 */
@Repository
public interface AddressBookCollectionRepository extends JpaRepository<AddressBookCollection, Long> {

    /**
     * Find address book collections by user ID
     *
     * @param userId user ID
     * @return List of address book collections
     */
    List<AddressBookCollection> findByUserId(Long userId);
}

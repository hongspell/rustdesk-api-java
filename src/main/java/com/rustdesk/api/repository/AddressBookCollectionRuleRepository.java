package com.rustdesk.api.repository;

import com.rustdesk.api.entity.AddressBookCollectionRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * AddressBookCollectionRule repository interface
 */
@Repository
public interface AddressBookCollectionRuleRepository extends JpaRepository<AddressBookCollectionRule, Long> {

    /**
     * Find collection rules by collection ID
     *
     * @param collectionId collection ID
     * @return List of collection rules
     */
    List<AddressBookCollectionRule> findByCollectionId(Long collectionId);

    /**
     * Find collection rules by user ID
     *
     * @param userId user ID
     * @return List of collection rules
     */
    List<AddressBookCollectionRule> findByUserId(Long userId);

    /**
     * Find collection rule by collection ID, type and target ID
     *
     * @param collectionId collection ID
     * @param type rule type
     * @param toId target ID
     * @return Optional AddressBookCollectionRule
     */
    Optional<AddressBookCollectionRule> findByCollectionIdAndTypeAndToId(Long collectionId, Integer type, Long toId);
}

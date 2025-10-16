package com.rustdesk.api.repository;

import com.rustdesk.api.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Tag repository interface
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Find tags by user ID
     *
     * @param userId user ID
     * @return List of tags
     */
    List<Tag> findByUserId(Long userId);

    /**
     * Find tags by user ID and collection ID
     *
     * @param userId user ID
     * @param collectionId collection ID
     * @return List of tags
     */
    List<Tag> findByUserIdAndCollectionId(Long userId, Long collectionId);

    /**
     * Find tag by user ID, name and collection ID
     *
     * @param userId user ID
     * @param name tag name
     * @param collectionId collection ID
     * @return Optional Tag
     */
    Optional<Tag> findByUserIdAndNameAndCollectionId(Long userId, String name, Long collectionId);
}

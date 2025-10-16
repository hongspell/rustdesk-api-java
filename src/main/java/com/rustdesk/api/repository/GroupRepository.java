package com.rustdesk.api.repository;

import com.rustdesk.api.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Group repository interface
 */
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    /**
     * Find group by name
     *
     * @param name group name
     * @return Optional Group
     */
    Optional<Group> findByName(String name);

    /**
     * Find groups by type
     *
     * @param type group type
     * @return List of groups
     */
    List<Group> findByType(Integer type);
}

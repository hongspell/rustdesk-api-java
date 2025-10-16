package com.rustdesk.api.service;

import com.rustdesk.api.entity.Group;
import com.rustdesk.api.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Group Service
 * Manages user and peer groups including creation, updates, and queries.
 *
 * @author RustDesk API Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    /**
     * Find all groups
     *
     * @return list of all groups
     */
    @Transactional(readOnly = true)
    public List<Group> findAll() {
        log.debug("Finding all groups");
        return groupRepository.findAll();
    }

    /**
     * Find group by ID
     *
     * @param id group ID
     * @return Optional Group
     */
    @Transactional(readOnly = true)
    public Optional<Group> findById(Long id) {
        log.debug("Finding group by id: {}", id);
        return groupRepository.findById(id);
    }

    /**
     * Find group by name
     *
     * @param name group name
     * @return Optional Group
     */
    @Transactional(readOnly = true)
    public Optional<Group> findByName(String name) {
        log.debug("Finding group by name: {}", name);
        return groupRepository.findByName(name);
    }

    /**
     * Find groups by type
     *
     * @param type group type
     * @return list of groups
     */
    @Transactional(readOnly = true)
    public List<Group> findByType(Integer type) {
        log.debug("Finding groups by type: {}", type);
        return groupRepository.findByType(type);
    }

    /**
     * Create a new group
     *
     * @param group group entity
     * @return created group
     * @throws IllegalArgumentException if group name already exists
     */
    @Transactional
    public Group createGroup(Group group) {
        log.info("Creating new group: {}", group.getName());

        // Check if group name already exists
        Optional<Group> existingOpt = groupRepository.findByName(group.getName());
        if (existingOpt.isPresent()) {
            log.warn("Group name already exists: {}", group.getName());
            throw new IllegalArgumentException("Group name already exists: " + group.getName());
        }

        Group saved = groupRepository.save(group);
        log.info("Group created with id: {}", saved.getId());
        return saved;
    }

    /**
     * Update an existing group
     *
     * @param group group entity with updated information
     * @return updated group
     * @throws IllegalArgumentException if group not found or name conflict
     */
    @Transactional
    public Group updateGroup(Group group) {
        log.info("Updating group: {}", group.getId());

        if (!groupRepository.existsById(group.getId())) {
            log.warn("Group not found with id: {}", group.getId());
            throw new IllegalArgumentException("Group not found with id: " + group.getId());
        }

        // Check if new name conflicts with existing group
        Optional<Group> existingOpt = groupRepository.findByName(group.getName());
        if (existingOpt.isPresent() && !existingOpt.get().getId().equals(group.getId())) {
            log.warn("Group name already exists: {}", group.getName());
            throw new IllegalArgumentException("Group name already exists: " + group.getName());
        }

        Group saved = groupRepository.save(group);
        log.info("Group updated successfully: {}", saved.getId());
        return saved;
    }

    /**
     * Delete a group
     * Note: This is a hard delete. Consider checking for dependencies before deletion.
     *
     * @param id group ID
     * @throws IllegalArgumentException if group not found
     */
    @Transactional
    public void deleteGroup(Long id) {
        log.info("Deleting group with id: {}", id);

        if (!groupRepository.existsById(id)) {
            log.warn("Group not found with id: {}", id);
            throw new IllegalArgumentException("Group not found with id: " + id);
        }

        groupRepository.deleteById(id);
        log.info("Group deleted successfully: {}", id);
    }

    /**
     * Check if group exists by name
     *
     * @param name group name
     * @return true if group exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return groupRepository.findByName(name).isPresent();
    }

    /**
     * Check if group exists by ID
     *
     * @param id group ID
     * @return true if group exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return groupRepository.existsById(id);
    }

    /**
     * Get or create group by name
     * Returns existing group if found, creates new one otherwise.
     *
     * @param name group name
     * @param type group type
     * @return existing or newly created group
     */
    @Transactional
    public Group getOrCreateGroup(String name, Integer type) {
        log.debug("Getting or creating group: {}", name);

        Optional<Group> existingOpt = groupRepository.findByName(name);
        if (existingOpt.isPresent()) {
            log.debug("Group already exists: {}", name);
            return existingOpt.get();
        }

        Group group = new Group();
        group.setName(name);
        group.setType(type);

        Group saved = groupRepository.save(group);
        log.info("New group created: {} with id: {}", name, saved.getId());
        return saved;
    }
}

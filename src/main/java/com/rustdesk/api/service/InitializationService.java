package com.rustdesk.api.service;

import com.rustdesk.api.entity.Group;
import com.rustdesk.api.entity.User;
import com.rustdesk.api.repository.GroupRepository;
import com.rustdesk.api.repository.UserRepository;
import com.rustdesk.api.util.PasswordUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Initialization Service
 * Handles application startup initialization including default users and groups.
 *
 * @author RustDesk API Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InitializationService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    private static final String DEFAULT_ADMIN_USERNAME = "admin";
    private static final String DEFAULT_ADMIN_PASSWORD = "admin@123";
    private static final String DEFAULT_GROUP_NAME = "Default Group";
    private static final Integer DEFAULT_GROUP_TYPE = 1;

    /**
     * Initialize the application
     * This method is called automatically after the service is constructed.
     * It creates default groups and admin user if they don't exist.
     */
    @PostConstruct
    @Transactional
    public void init() {
        log.info("Starting application initialization...");

        try {
            // Check and create default groups
            checkAndCreateDefaultGroups();

            // Check and create default admin user
            checkAndCreateDefaultAdmin();

            log.info("Application initialization completed successfully");
        } catch (Exception e) {
            log.error("Error during application initialization", e);
            // Don't throw exception to prevent application startup failure
        }
    }

    /**
     * Check and create default groups if they don't exist
     * Creates a default group for organizing users and peers.
     */
    @Transactional
    public void checkAndCreateDefaultGroups() {
        log.info("Checking default groups...");

        Optional<Group> defaultGroupOpt = groupRepository.findByName(DEFAULT_GROUP_NAME);
        if (defaultGroupOpt.isEmpty()) {
            log.info("Default group not found, creating...");

            Group defaultGroup = new Group();
            defaultGroup.setName(DEFAULT_GROUP_NAME);
            defaultGroup.setType(DEFAULT_GROUP_TYPE);

            Group saved = groupRepository.save(defaultGroup);
            log.info("Default group created with id: {}", saved.getId());
        } else {
            log.info("Default group already exists with id: {}", defaultGroupOpt.get().getId());
        }
    }

    /**
     * Check and create default admin user if it doesn't exist
     * Creates a default administrator account with predefined credentials.
     */
    @Transactional
    public void checkAndCreateDefaultAdmin() {
        log.info("Checking default admin user...");

        Optional<User> adminOpt = userRepository.findByUsername(DEFAULT_ADMIN_USERNAME);
        if (adminOpt.isEmpty()) {
            log.info("Default admin user not found, creating...");

            User admin = new User();
            admin.setUsername(DEFAULT_ADMIN_USERNAME);
            admin.setPassword(PasswordUtil.encryptPassword(DEFAULT_ADMIN_PASSWORD));
            admin.setNickname("Administrator");
            admin.setIsAdmin(true);
            admin.setStatus(1);

            // Assign to default group
            Optional<Group> defaultGroupOpt = groupRepository.findByName(DEFAULT_GROUP_NAME);
            defaultGroupOpt.ifPresent(group -> admin.setGroupId(group.getId()));

            User saved = userRepository.save(admin);
            log.info("Default admin user created with id: {}", saved.getId());

            // Print admin password for initial setup
            printAdminPassword();
        } else {
            log.info("Default admin user already exists with id: {}", adminOpt.get().getId());
        }
    }

    /**
     * Print admin password to console
     * Displays the default admin credentials when the admin account is first created.
     * This helps administrators know the initial login credentials.
     */
    public void printAdminPassword() {
        log.info("=".repeat(80));
        log.info("DEFAULT ADMIN CREDENTIALS");
        log.info("=".repeat(80));
        log.info("Username: {}", DEFAULT_ADMIN_USERNAME);
        log.info("Password: {}", DEFAULT_ADMIN_PASSWORD);
        log.info("=".repeat(80));
        log.warn("IMPORTANT: Please change the admin password after first login!");
        log.info("=".repeat(80));
    }

    /**
     * Reset admin password to default
     * WARNING: This method should only be used for emergency recovery.
     *
     * @return true if reset successful, false otherwise
     */
    @Transactional
    public boolean resetAdminPassword() {
        log.warn("Resetting admin password to default...");

        Optional<User> adminOpt = userRepository.findByUsername(DEFAULT_ADMIN_USERNAME);
        if (adminOpt.isEmpty()) {
            log.error("Admin user not found");
            return false;
        }

        User admin = adminOpt.get();
        admin.setPassword(PasswordUtil.encryptPassword(DEFAULT_ADMIN_PASSWORD));
        userRepository.save(admin);

        log.warn("Admin password has been reset to default");
        printAdminPassword();
        return true;
    }

    /**
     * Create a custom group
     * Convenience method to create groups during initialization.
     *
     * @param name group name
     * @param type group type
     * @return created group
     */
    @Transactional
    public Group createGroup(String name, Integer type) {
        log.info("Creating custom group: {}", name);

        Optional<Group> existingOpt = groupRepository.findByName(name);
        if (existingOpt.isPresent()) {
            log.info("Group already exists: {}", name);
            return existingOpt.get();
        }

        Group group = new Group();
        group.setName(name);
        group.setType(type);

        Group saved = groupRepository.save(group);
        log.info("Custom group created with id: {}", saved.getId());
        return saved;
    }

    /**
     * Create additional administrator user
     *
     * @param username username
     * @param password password (will be encoded)
     * @param email email address
     * @return created admin user
     */
    @Transactional
    public User createAdminUser(String username, String password, String email) {
        log.info("Creating additional admin user: {}", username);

        if (userRepository.existsByUsername(username)) {
            log.warn("Username already exists: {}", username);
            throw new IllegalArgumentException("Username already exists: " + username);
        }

        User admin = new User();
        admin.setUsername(username);
        admin.setPassword(PasswordUtil.encryptPassword(password));
        admin.setEmail(email);
        admin.setIsAdmin(true);
        admin.setStatus(1);

        // Assign to default group
        Optional<Group> defaultGroupOpt = groupRepository.findByName(DEFAULT_GROUP_NAME);
        defaultGroupOpt.ifPresent(group -> admin.setGroupId(group.getId()));

        User saved = userRepository.save(admin);
        log.info("Admin user created with id: {}", saved.getId());
        return saved;
    }

    /**
     * Check if system is initialized
     * Verifies that default groups and admin user exist.
     *
     * @return true if system is initialized, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean isInitialized() {
        boolean hasDefaultGroup = groupRepository.findByName(DEFAULT_GROUP_NAME).isPresent();
        boolean hasAdminUser = userRepository.findByUsername(DEFAULT_ADMIN_USERNAME).isPresent();

        boolean initialized = hasDefaultGroup && hasAdminUser;
        log.debug("System initialization status: {}", initialized);
        return initialized;
    }
}

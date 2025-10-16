package com.rustdesk.api.service;

import com.rustdesk.api.entity.LoginLog;
import com.rustdesk.api.repository.LoginLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Login Log Service
 * Manages login logs including creation, queries, and cleanup.
 *
 * @author RustDesk API Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginLogService {

    private final LoginLogRepository loginLogRepository;

    /**
     * Create a new login log entry
     *
     * @param loginLog login log entity
     * @return created login log
     */
    @Transactional
    public LoginLog createLog(LoginLog loginLog) {
        log.info("Creating login log for user: {} with client: {}", loginLog.getUserId(), loginLog.getClient());

        // Set default values if not provided
        if (loginLog.getIsDeleted() == null) {
            loginLog.setIsDeleted(false);
        }

        LoginLog saved = loginLogRepository.save(loginLog);
        log.debug("Login log created with id: {}", saved.getId());
        return saved;
    }

    /**
     * Find login logs by user ID
     *
     * @param userId user ID
     * @return list of login logs
     */
    @Transactional(readOnly = true)
    public List<LoginLog> findByUserId(Long userId) {
        log.debug("Finding login logs for user: {}", userId);
        return loginLogRepository.findByUserId(userId);
    }

    /**
     * Delete a login log entry (soft delete)
     * Marks the log entry as deleted instead of removing it from the database.
     *
     * @param id login log ID
     * @return updated login log
     * @throws IllegalArgumentException if log not found
     */
    @Transactional
    public LoginLog deleteLog(Long id) {
        log.info("Soft deleting login log: {}", id);

        Optional<LoginLog> logOpt = loginLogRepository.findById(id);
        if (logOpt.isEmpty()) {
            log.warn("Login log not found with id: {}", id);
            throw new IllegalArgumentException("Login log not found with id: " + id);
        }

        LoginLog loginLog = logOpt.get();
        loginLog.setIsDeleted(true);

        LoginLog saved = loginLogRepository.save(loginLog);
        log.info("Login log soft deleted: {}", id);
        return saved;
    }

    /**
     * Hard delete a login log entry
     * Permanently removes the log entry from the database.
     *
     * @param id login log ID
     */
    @Transactional
    public void hardDeleteLog(Long id) {
        log.info("Hard deleting login log: {}", id);

        if (!loginLogRepository.existsById(id)) {
            log.warn("Login log not found with id: {}", id);
            throw new IllegalArgumentException("Login log not found with id: " + id);
        }

        loginLogRepository.deleteById(id);
        log.info("Login log hard deleted: {}", id);
    }

    /**
     * Find login logs by deleted status with pagination
     *
     * @param isDeleted deleted status (false: not deleted, true: deleted)
     * @param pageable pagination parameters
     * @return page of login logs
     */
    @Transactional(readOnly = true)
    public Page<LoginLog> findByIsDeleted(Boolean isDeleted, Pageable pageable) {
        log.debug("Finding login logs with isDeleted: {}", isDeleted);
        // Convert Boolean to Integer (0 or 1) as per repository signature
        Integer isDeletedInt = isDeleted ? 1 : 0;
        return loginLogRepository.findByIsDeleted(isDeletedInt, pageable);
    }

    /**
     * Find login log by ID
     *
     * @param id login log ID
     * @return Optional LoginLog
     */
    @Transactional(readOnly = true)
    public Optional<LoginLog> findById(Long id) {
        log.debug("Finding login log by id: {}", id);
        return loginLogRepository.findById(id);
    }

    /**
     * Find all login logs
     *
     * @return list of all login logs
     */
    @Transactional(readOnly = true)
    public List<LoginLog> findAll() {
        log.debug("Finding all login logs");
        return loginLogRepository.findAll();
    }

    /**
     * Create login log with builder pattern support
     * Convenience method to create a login log with common parameters.
     *
     * @param userId user ID
     * @param client client type (webadmin, webclient, app)
     * @param type login type (account, oauth)
     * @param ip IP address
     * @param deviceId device ID
     * @param uuid unique identifier
     * @return created login log
     */
    @Transactional
    public LoginLog createLog(Long userId, String client, String type, String ip, String deviceId, String uuid) {
        log.info("Creating login log for user: {} from client: {}", userId, client);

        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(userId);
        loginLog.setClient(client);
        loginLog.setType(type);
        loginLog.setIp(ip);
        loginLog.setDeviceId(deviceId);
        loginLog.setUuid(uuid);
        loginLog.setIsDeleted(false);

        return createLog(loginLog);
    }

    /**
     * Delete all logs for a user
     *
     * @param userId user ID
     */
    @Transactional
    public void deleteByUserId(Long userId) {
        log.info("Soft deleting all login logs for user: {}", userId);
        List<LoginLog> logs = findByUserId(userId);
        for (LoginLog log : logs) {
            log.setIsDeleted(true);
        }
        loginLogRepository.saveAll(logs);
        log.info("Soft deleted {} login logs for user: {}", logs.size(), userId);
    }

    /**
     * Count total login logs
     *
     * @return total count
     */
    @Transactional(readOnly = true)
    public long count() {
        return loginLogRepository.count();
    }

    /**
     * Batch create login logs
     *
     * @param logs list of login logs to create
     * @return list of created login logs
     */
    @Transactional
    public List<LoginLog> batchCreateLogs(List<LoginLog> logs) {
        log.info("Batch creating {} login logs", logs.size());

        // Set default values
        for (LoginLog log : logs) {
            if (log.getIsDeleted() == null) {
                log.setIsDeleted(false);
            }
        }

        List<LoginLog> saved = loginLogRepository.saveAll(logs);
        log.info("Batch created {} login logs", saved.size());
        return saved;
    }
}

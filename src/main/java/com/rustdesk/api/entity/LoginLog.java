package com.rustdesk.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Login Log Entity
 *
 * @author RustDesk API Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "login_log", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_client", columnList = "client"),
    @Index(name = "idx_device_id", columnList = "device_id"),
    @Index(name = "idx_uuid", columnList = "uuid"),
    @Index(name = "idx_type", columnList = "type"),
    @Index(name = "idx_is_deleted", columnList = "is_deleted"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@EntityListeners(AuditingEntityListener.class)
public class LoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User ID (foreign key to user table)
     */
    @NotNull(message = "User ID cannot be null")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * Client type: webadmin, webclient, app
     */
    @NotBlank(message = "Client type cannot be blank")
    @Column(name = "client", nullable = false, length = 50)
    private String client;

    /**
     * Device ID
     */
    @Column(name = "device_id", length = 255)
    private String deviceId;

    /**
     * Unique identifier (UUID)
     */
    @Column(name = "uuid", length = 255)
    private String uuid;

    /**
     * IP address
     */
    @Column(name = "ip", length = 100)
    private String ip;

    /**
     * Login type: account, oauth
     */
    @NotBlank(message = "Login type cannot be blank")
    @Column(name = "type", nullable = false, length = 50)
    private String type;

    /**
     * Platform (operating system/browser)
     */
    @Column(name = "platform", length = 255)
    private String platform;

    /**
     * User Token ID (foreign key to user_token table)
     */
    @Column(name = "user_token_id")
    private Long userTokenId;

    /**
     * Soft delete flag
     */
    @NotNull(message = "IsDeleted flag cannot be null")
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    /**
     * Created timestamp
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Updated timestamp
     */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

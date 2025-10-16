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
 * Share Record Entity
 *
 * @author RustDesk API Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "share_record", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_peer_id", columnList = "peer_id"),
    @Index(name = "idx_share_token", columnList = "share_token", unique = true),
    @Index(name = "idx_expire", columnList = "expire"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@EntityListeners(AuditingEntityListener.class)
public class ShareRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User ID (owner of the share)
     */
    @NotNull(message = "User ID cannot be null")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * Peer ID (shared device)
     */
    @NotBlank(message = "Peer ID cannot be blank")
    @Column(name = "peer_id", nullable = false, length = 255)
    private String peerId;

    /**
     * Share Token (UUID for access)
     */
    @NotBlank(message = "Share token cannot be blank")
    @Column(name = "share_token", nullable = false, length = 255, unique = true)
    private String shareToken;

    /**
     * Password type (0=no password, 1=fixed password, 2=one-time password)
     */
    @NotNull(message = "Password type cannot be null")
    @Column(name = "password_type", nullable = false)
    private Integer passwordType = 0;

    /**
     * Password (encrypted)
     */
    @Column(name = "password", length = 500)
    private String password;

    /**
     * Expiration time (Unix timestamp in milliseconds)
     */
    @Column(name = "expire")
    private Long expire;

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

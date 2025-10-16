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
 * Third-party Account Binding Entity
 *
 * @author RustDesk API Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_third", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_open_id", columnList = "open_id"),
    @Index(name = "idx_op", columnList = "op"),
    @Index(name = "idx_oauth_type", columnList = "oauth_type")
})
@EntityListeners(AuditingEntityListener.class)
public class UserThird {

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
     * OpenID from third-party provider
     */
    @NotBlank(message = "OpenID cannot be blank")
    @Column(name = "open_id", nullable = false, length = 255)
    private String openId;

    /**
     * Display name from third-party provider
     */
    @Column(name = "name", length = 255)
    private String name;

    /**
     * Username from third-party provider
     */
    @Column(name = "username", length = 255)
    private String username;

    /**
     * Email from third-party provider
     */
    @Column(name = "email", length = 255)
    private String email;

    /**
     * Whether email is verified by third-party provider
     */
    @Column(name = "verified_email")
    private Boolean verifiedEmail = false;

    /**
     * Profile picture URL from third-party provider
     */
    @Column(name = "picture", length = 1000)
    private String picture;

    /**
     * UnionID from third-party provider (for multi-app scenarios)
     */
    @Column(name = "union_id", length = 255)
    private String unionId;

    /**
     * OAuth Type: github, google, oidc, webauth
     */
    @NotBlank(message = "OAuth type cannot be blank")
    @Column(name = "oauth_type", nullable = false, length = 50)
    private String oauthType;

    /**
     * OAuth Provider identifier
     */
    @NotBlank(message = "Provider identifier cannot be blank")
    @Column(name = "op", nullable = false, length = 100)
    private String op;

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

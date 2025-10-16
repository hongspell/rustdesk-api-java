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
 * OAuth Configuration Entity
 *
 * @author RustDesk API Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "oauth", indexes = {
    @Index(name = "idx_op", columnList = "op"),
    @Index(name = "idx_oauth_type", columnList = "oauth_type")
})
@EntityListeners(AuditingEntityListener.class)
public class Oauth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * OAuth Provider identifier
     */
    @NotBlank(message = "Provider identifier cannot be blank")
    @Column(name = "op", nullable = false, length = 100)
    private String op;

    /**
     * OAuth Type: github, google, oidc, webauth
     */
    @NotBlank(message = "OAuth type cannot be blank")
    @Column(name = "oauth_type", nullable = false, length = 50)
    private String oauthType;

    /**
     * OAuth Client ID
     */
    @NotBlank(message = "Client ID cannot be blank")
    @Column(name = "client_id", nullable = false, length = 500)
    private String clientId;

    /**
     * OAuth Client Secret
     */
    @Column(name = "client_secret", length = 500)
    private String clientSecret;

    /**
     * Auto register user on first login
     */
    @NotNull(message = "Auto register flag cannot be null")
    @Column(name = "auto_register", nullable = false)
    private Boolean autoRegister = false;

    /**
     * OAuth Scopes (comma-separated)
     */
    @Column(name = "scopes", columnDefinition = "TEXT")
    private String scopes;

    /**
     * OIDC Issuer URL
     */
    @Column(name = "issuer", length = 500)
    private String issuer;

    /**
     * Enable PKCE (Proof Key for Code Exchange)
     */
    @NotNull(message = "PKCE enable flag cannot be null")
    @Column(name = "pkce_enable", nullable = false)
    private Boolean pkceEnable = false;

    /**
     * PKCE Method: S256 or plain
     */
    @Column(name = "pkce_method", length = 10)
    private String pkceMethod;

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

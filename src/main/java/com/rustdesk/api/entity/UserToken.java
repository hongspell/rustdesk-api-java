package com.rustdesk.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * User Token entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_tokens", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_token", columnList = "token", unique = true),
        @Index(name = "idx_device_uuid", columnList = "device_uuid"),
        @Index(name = "idx_expired_at", columnList = "expired_at")
})
public class UserToken extends BaseEntity {

    @NotNull(message = "User ID cannot be null")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Size(max = 100, message = "Device UUID must not exceed 100 characters")
    @Column(name = "device_uuid", length = 100)
    private String deviceUuid;

    @Size(max = 100, message = "Device ID must not exceed 100 characters")
    @Column(name = "device_id", length = 100)
    private String deviceId;

    @NotBlank(message = "Token cannot be blank")
    @Size(max = 500, message = "Token must not exceed 500 characters")
    @Column(name = "token", nullable = false, unique = true, length = 500)
    private String token;

    @NotNull(message = "Expired at cannot be null")
    @Column(name = "expired_at", nullable = false)
    private Long expiredAt;  // Unix timestamp in milliseconds
}

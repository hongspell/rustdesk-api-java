package com.rustdesk.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

/**
 * Address Book entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "address_books", indexes = {
        @Index(name = "idx_device_id", columnList = "device_id"),
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_collection_id", columnList = "collection_id"),
        @Index(name = "idx_hash", columnList = "hash")
})
public class AddressBook extends BaseEntity {

    @Column(name = "row_id")
    private Long rowId;

    @NotBlank(message = "Device ID cannot be blank")
    @Size(max = 100, message = "Device ID must not exceed 100 characters")
    @Column(name = "device_id", nullable = false, length = 100)
    private String deviceId;

    @Column(name = "user_id")
    private Long userId;

    @Size(max = 100, message = "Username must not exceed 100 characters")
    @Column(name = "username", length = 100)
    private String username;

    @Size(max = 255, message = "Password must not exceed 255 characters")
    @Column(name = "password", length = 255)
    private String password;

    @Size(max = 200, message = "Hostname must not exceed 200 characters")
    @Column(name = "hostname", length = 200)
    private String hostname;

    @Size(max = 200, message = "Alias must not exceed 200 characters")
    @Column(name = "alias", length = 200)
    private String alias;

    @Size(max = 50, message = "Platform must not exceed 50 characters")
    @Column(name = "platform", length = 50)
    private String platform;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tags", columnDefinition = "json")
    private List<String> tags;

    @Size(max = 255, message = "Hash must not exceed 255 characters")
    @Column(name = "hash", length = 255)
    private String hash;

    @Column(name = "collection_id")
    private Long collectionId;

    @Column(name = "force_always_relay")
    private Boolean forceAlwaysRelay = false;

    @Column(name = "rdp_port")
    private Integer rdpPort;

    @Size(max = 100, message = "RDP username must not exceed 100 characters")
    @Column(name = "rdp_username", length = 100)
    private String rdpUsername;

    @Column(name = "online")
    private Boolean online = false;

    @Size(max = 100, message = "Login name must not exceed 100 characters")
    @Column(name = "login_name", length = 100)
    private String loginName;

    @Column(name = "same_server")
    private Boolean sameServer = false;
}

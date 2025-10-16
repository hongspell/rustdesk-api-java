package com.rustdesk.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Peer (Device) entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "peers", indexes = {
        @Index(name = "idx_device_id", columnList = "device_id", unique = true),
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_group_id", columnList = "group_id"),
        @Index(name = "idx_uuid", columnList = "uuid")
})
public class Peer extends BaseEntity {

    @Column(name = "row_id")
    private Long rowId;

    @NotBlank(message = "Device ID cannot be blank")
    @Size(max = 100, message = "Device ID must not exceed 100 characters")
    @Column(name = "device_id", nullable = false, unique = true, length = 100)
    private String deviceId;

    @Size(max = 200, message = "CPU info must not exceed 200 characters")
    @Column(name = "cpu", length = 200)
    private String cpu;

    @Size(max = 200, message = "Hostname must not exceed 200 characters")
    @Column(name = "hostname", length = 200)
    private String hostname;

    @Size(max = 100, message = "Memory info must not exceed 100 characters")
    @Column(name = "memory", length = 100)
    private String memory;

    @Size(max = 100, message = "OS info must not exceed 100 characters")
    @Column(name = "os", length = 100)
    private String os;

    @Size(max = 100, message = "Username must not exceed 100 characters")
    @Column(name = "username", length = 100)
    private String username;

    @Size(max = 100, message = "UUID must not exceed 100 characters")
    @Column(name = "uuid", length = 100)
    private String uuid;

    @Size(max = 50, message = "Version must not exceed 50 characters")
    @Column(name = "version", length = 50)
    private String version;

    @Column(name = "user_id")
    private Long userId;

    /**
     * Last online time (Unix timestamp in milliseconds)
     */
    @Column(name = "last_online_time")
    private Long lastOnlineTime;

    @Size(max = 50, message = "Last online IP must not exceed 50 characters")
    @Column(name = "last_online_ip", length = 50)
    private String lastOnlineIp;

    @Column(name = "group_id")
    private Long groupId;

    @Size(max = 200, message = "Alias must not exceed 200 characters")
    @Column(name = "alias", length = 200)
    private String alias;
}

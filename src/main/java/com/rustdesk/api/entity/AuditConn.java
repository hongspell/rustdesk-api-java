package com.rustdesk.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Connection Audit Entity
 *
 * @author RustDesk API Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "audit_conn", indexes = {
    @Index(name = "idx_conn_id", columnList = "conn_id"),
    @Index(name = "idx_peer_id", columnList = "peer_id"),
    @Index(name = "idx_from_peer", columnList = "from_peer"),
    @Index(name = "idx_action", columnList = "action"),
    @Index(name = "idx_session_id", columnList = "session_id"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@EntityListeners(AuditingEntityListener.class)
public class AuditConn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Action type: new, close
     */
    @NotBlank(message = "Action cannot be blank")
    @Column(name = "action", nullable = false, length = 50)
    private String action;

    /**
     * Connection ID
     */
    @NotBlank(message = "Connection ID cannot be blank")
    @Column(name = "conn_id", nullable = false, length = 255)
    private String connId;

    /**
     * Peer ID (target device)
     */
    @NotBlank(message = "Peer ID cannot be blank")
    @Column(name = "peer_id", nullable = false, length = 255)
    private String peerId;

    /**
     * Source Peer ID
     */
    @Column(name = "from_peer", length = 255)
    private String fromPeer;

    /**
     * Source Peer name
     */
    @Column(name = "from_name", length = 255)
    private String fromName;

    /**
     * IP address
     */
    @Column(name = "ip", length = 100)
    private String ip;

    /**
     * Session ID
     */
    @Column(name = "session_id", length = 255)
    private String sessionId;

    /**
     * Connection type
     */
    @Column(name = "type", length = 100)
    private String type;

    /**
     * Unique identifier (UUID)
     */
    @Column(name = "uuid", length = 255)
    private String uuid;

    /**
     * Connection close time (Unix timestamp in milliseconds)
     */
    @Column(name = "close_time")
    private Long closeTime;

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

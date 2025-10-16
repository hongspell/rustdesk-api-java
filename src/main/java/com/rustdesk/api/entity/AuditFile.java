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
 * File Transfer Audit Entity
 *
 * @author RustDesk API Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "audit_file", indexes = {
    @Index(name = "idx_from_peer", columnList = "from_peer"),
    @Index(name = "idx_peer_id", columnList = "peer_id"),
    @Index(name = "idx_uuid", columnList = "uuid"),
    @Index(name = "idx_is_file", columnList = "is_file"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@EntityListeners(AuditingEntityListener.class)
public class AuditFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Source Peer ID
     */
    @NotBlank(message = "From Peer ID cannot be blank")
    @Column(name = "from_peer", nullable = false, length = 255)
    private String fromPeer;

    /**
     * File/Directory information (metadata JSON)
     */
    @Column(name = "info", columnDefinition = "TEXT")
    private String info;

    /**
     * Whether it's a file (true) or directory (false)
     */
    @Column(name = "is_file", nullable = false)
    private Boolean isFile = true;

    /**
     * File/Directory path
     */
    @Column(name = "path", columnDefinition = "TEXT")
    private String path;

    /**
     * Target Peer ID
     */
    @NotBlank(message = "Peer ID cannot be blank")
    @Column(name = "peer_id", nullable = false, length = 255)
    private String peerId;

    /**
     * Transfer type (send/receive)
     */
    @Column(name = "type", length = 50)
    private String type;

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
     * Number of files (for batch operations)
     */
    @Column(name = "num")
    private Integer num;

    /**
     * Source Peer name
     */
    @Column(name = "from_name", length = 255)
    private String fromName;

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

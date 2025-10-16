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
 * Server Command Entity
 *
 * @author RustDesk API Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "server_cmd", indexes = {
    @Index(name = "idx_cmd", columnList = "cmd"),
    @Index(name = "idx_target", columnList = "target"),
    @Index(name = "idx_alias", columnList = "alias")
})
@EntityListeners(AuditingEntityListener.class)
public class ServerCmd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Command name
     */
    @NotBlank(message = "Command cannot be blank")
    @Column(name = "cmd", nullable = false, length = 255)
    private String cmd;

    /**
     * Command alias (display name)
     */
    @Column(name = "alias", length = 255)
    private String alias;

    /**
     * Command options (JSON format)
     */
    @Column(name = "option", columnDefinition = "TEXT")
    private String option;

    /**
     * Command explanation/description
     */
    @Column(name = "explain", columnDefinition = "TEXT")
    private String explain;

    /**
     * Target server:
     * 21115 = ID Server
     * 21117 = Relay Server
     */
    @NotNull(message = "Target cannot be null")
    @Column(name = "target", nullable = false)
    private Integer target;

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

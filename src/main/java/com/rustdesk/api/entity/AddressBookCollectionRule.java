package com.rustdesk.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Address Book Collection Sharing Rule Entity
 *
 * @author RustDesk API Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address_book_collection_rule", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_collection_id", columnList = "collection_id"),
    @Index(name = "idx_to_id", columnList = "to_id"),
    @Index(name = "idx_type", columnList = "type"),
    @Index(name = "idx_rule", columnList = "rule")
})
@EntityListeners(AuditingEntityListener.class)
public class AddressBookCollectionRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User ID (owner of the collection)
     */
    @NotNull(message = "User ID cannot be null")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * Collection ID (foreign key to address_book_collection)
     */
    @NotNull(message = "Collection ID cannot be null")
    @Column(name = "collection_id", nullable = false)
    private Long collectionId;

    /**
     * Permission rule:
     * 1 = Read only
     * 2 = Read and Write
     * 3 = Full Control
     */
    @NotNull(message = "Rule cannot be null")
    @Column(name = "rule", nullable = false)
    private Integer rule;

    /**
     * Target type:
     * 1 = Individual user
     * 2 = Group
     */
    @NotNull(message = "Type cannot be null")
    @Column(name = "type", nullable = false)
    private Integer type;

    /**
     * Target ID (User ID or Group ID)
     */
    @NotNull(message = "Target ID cannot be null")
    @Column(name = "to_id", nullable = false)
    private Long toId;

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

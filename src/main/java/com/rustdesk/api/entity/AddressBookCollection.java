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
 * Address Book Collection Entity
 *
 * @author RustDesk API Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address_book_collection", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_name", columnList = "name"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@EntityListeners(AuditingEntityListener.class)
public class AddressBookCollection {

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
     * Collection name
     */
    @NotBlank(message = "Collection name cannot be blank")
    @Column(name = "name", nullable = false, length = 255)
    private String name;

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

package com.rustdesk.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Tag entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tags", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_collection_id", columnList = "collection_id"),
        @Index(name = "idx_user_collection", columnList = "user_id,collection_id")
})
public class Tag extends BaseEntity {

    @NotBlank(message = "Tag name cannot be blank")
    @Size(max = 100, message = "Tag name must not exceed 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "user_id")
    private Long userId;

    @Size(max = 20, message = "Color must not exceed 20 characters")
    @Column(name = "color", length = 20)
    private String color;

    @Column(name = "collection_id")
    private Long collectionId;
}

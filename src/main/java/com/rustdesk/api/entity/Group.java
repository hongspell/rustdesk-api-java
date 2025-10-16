package com.rustdesk.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Group entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "groups", indexes = {
        @Index(name = "idx_name", columnList = "name"),
        @Index(name = "idx_type", columnList = "type")
})
public class Group extends BaseEntity {

    @NotBlank(message = "Group name cannot be blank")
    @Size(max = 100, message = "Group name must not exceed 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotNull(message = "Group type cannot be null")
    @Column(name = "type", nullable = false)
    private Integer type;
}

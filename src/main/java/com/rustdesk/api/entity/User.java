package com.rustdesk.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * User entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_username", columnList = "username", unique = true),
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_group_id", columnList = "group_id")
})
public class User extends BaseEntity {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(name = "email", length = 100)
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Size(max = 100, message = "Nickname must not exceed 100 characters")
    @Column(name = "nickname", length = 100)
    private String nickname;

    @Size(max = 500, message = "Avatar URL must not exceed 500 characters")
    @Column(name = "avatar", length = 500)
    private String avatar;

    @Column(name = "group_id")
    private Long groupId;

    @NotNull(message = "IsAdmin flag cannot be null")
    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin = false;

    @NotNull(message = "Status cannot be null")
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @Size(max = 500, message = "Remark must not exceed 500 characters")
    @Column(name = "remark", length = 500)
    private String remark;
}

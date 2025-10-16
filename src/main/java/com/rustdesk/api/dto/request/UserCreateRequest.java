package com.rustdesk.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Create Request DTO
 *
 * @author RustDesk
 * @version 2.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User Create Request")
public class UserCreateRequest {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Schema(description = "Username", example = "newuser", required = true)
    private String username;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Schema(description = "Email", example = "user@example.com", required = true)
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Schema(description = "Password", example = "password123", required = true)
    private String password;

    @Schema(description = "Nickname", example = "John Doe")
    private String nickname;

    @Schema(description = "Group ID", example = "1")
    private Long groupId;

    @NotNull(message = "IsAdmin flag cannot be null")
    @Schema(description = "Is Admin", example = "false", required = true)
    private Boolean isAdmin;

    @NotNull(message = "Status cannot be null")
    @Schema(description = "Status (1=active, 0=inactive)", example = "1", required = true)
    private Integer status;
}

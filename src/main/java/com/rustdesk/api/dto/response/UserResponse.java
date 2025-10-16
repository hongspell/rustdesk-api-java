package com.rustdesk.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Response DTO
 * Excludes sensitive fields like password
 *
 * @author RustDesk
 * @version 2.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "User Response")
public class UserResponse {

    @Schema(description = "User ID", example = "1")
    private Long id;

    @Schema(description = "Username", example = "admin")
    private String username;

    @Schema(description = "Email", example = "admin@example.com")
    private String email;

    @Schema(description = "Nickname", example = "Administrator")
    private String nickname;

    @Schema(description = "Avatar URL", example = "https://example.com/avatar.jpg")
    private String avatar;

    @Schema(description = "Group ID", example = "1")
    private Long groupId;

    @Schema(description = "Is Admin", example = "true")
    private Boolean isAdmin;

    @Schema(description = "Status (1=active, 0=inactive)", example = "1")
    private Integer status;

    @Schema(description = "Remark", example = "System administrator")
    private String remark;
}

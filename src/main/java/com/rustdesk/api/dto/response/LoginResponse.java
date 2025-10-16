package com.rustdesk.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Login Response DTO
 *
 * @author RustDesk
 * @version 2.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login Response")
public class LoginResponse {

    @Schema(description = "Authentication Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "User Information")
    private UserResponse user;

    @Schema(description = "Token Expiration Time (Unix timestamp in milliseconds)", example = "1697654400000")
    private Long expiredAt;
}

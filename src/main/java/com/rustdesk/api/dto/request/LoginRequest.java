package com.rustdesk.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Login Request DTO
 *
 * @author RustDesk
 * @version 2.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login Request")
public class LoginRequest {

    @NotBlank(message = "Username cannot be blank")
    @Schema(description = "Username", example = "admin", required = true)
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Schema(description = "Password", example = "password123", required = true)
    private String password;

    @Schema(description = "Device ID", example = "device-123")
    private String deviceId;

    @Schema(description = "Device UUID", example = "uuid-456")
    private String deviceUuid;
}

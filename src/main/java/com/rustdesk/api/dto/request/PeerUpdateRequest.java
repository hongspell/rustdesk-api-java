package com.rustdesk.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Peer Update Request DTO
 *
 * @author RustDesk
 * @version 2.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Peer Update Request")
public class PeerUpdateRequest {

    @Schema(description = "Device ID", example = "device-123")
    private String deviceId;

    @Schema(description = "CPU Information", example = "Intel i7-9700K")
    private String cpu;

    @Schema(description = "Hostname", example = "DESKTOP-ABC123")
    private String hostname;

    @Schema(description = "Memory Information", example = "16GB")
    private String memory;

    @Schema(description = "Operating System", example = "Windows 10")
    private String os;

    @Schema(description = "Username", example = "user")
    private String username;

    @Schema(description = "UUID", example = "uuid-456")
    private String uuid;

    @Schema(description = "Version", example = "1.2.3")
    private String version;

    @Schema(description = "User ID", example = "1")
    private Long userId;
}

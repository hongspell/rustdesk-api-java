package com.rustdesk.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Peer Response DTO
 *
 * @author RustDesk
 * @version 2.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Peer Response")
public class PeerResponse {

    @Schema(description = "Peer ID", example = "1")
    private Long id;

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

    @Schema(description = "Last Online Time (timestamp in milliseconds)", example = "1697123456789")
    private Long lastOnlineTime;

    @Schema(description = "Last Online IP", example = "192.168.1.100")
    private String lastOnlineIp;

    @Schema(description = "Group ID", example = "1")
    private Long groupId;

    @Schema(description = "Alias", example = "My Desktop")
    private String alias;

    @Schema(description = "Online Status", example = "true")
    private Boolean online;
}

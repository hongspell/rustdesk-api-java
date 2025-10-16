package com.rustdesk.api.controller.api;

import com.rustdesk.api.dto.request.PeerUpdateRequest;
import com.rustdesk.api.dto.response.ApiResponse;
import com.rustdesk.api.dto.response.PeerResponse;
import com.rustdesk.api.entity.Peer;
import com.rustdesk.api.entity.User;
import com.rustdesk.api.service.PeerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Peer Controller
 * Handles device/peer-related operations
 *
 * @author RustDesk
 * @version 2.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
@Tag(name = "Peer", description = "Peer (Device) API")
@SecurityRequirement(name = "Bearer Authentication")
public class PeerController {

    private final PeerService peerService;

    /**
     * Report System Information
     * Create or update peer device information
     *
     * @param request Peer update request
     * @param httpRequest HTTP request
     * @return Success response
     */
    @PostMapping("/sysinfo")
    @Operation(summary = "Report System Info", description = "Create or update peer device information")
    public ApiResponse<Void> reportSysInfo(
            @Validated @RequestBody PeerUpdateRequest request,
            HttpServletRequest httpRequest) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            log.warn("Failed to report sysinfo: No valid token");
            return ApiResponse.unauthorized("Authentication required");
        }

        log.info("Reporting sysinfo for device: {}", request.getDeviceId());

        // Find existing peer or create new one
        Peer peer = peerService.findByDeviceId(request.getDeviceId())
                .orElse(null);
        if (peer == null) {
            peer = new Peer();
            peer.setDeviceId(request.getDeviceId());
            log.info("Creating new peer device: {}", request.getDeviceId());
        } else {
            log.debug("Updating existing peer device: {}", request.getDeviceId());
        }

        // Update peer information
        peer.setCpu(request.getCpu());
        peer.setHostname(request.getHostname());
        peer.setMemory(request.getMemory());
        peer.setOs(request.getOs());
        peer.setUsername(request.getUsername());
        peer.setUuid(request.getUuid());
        peer.setVersion(request.getVersion());
        peer.setUserId(userId);
        peer.setLastOnlineTime(System.currentTimeMillis());
        peer.setLastOnlineIp(httpRequest.getRemoteAddr());

        peerService.save(peer);

        log.info("Sysinfo reported successfully for device: {}", request.getDeviceId());
        return ApiResponse.success("System information updated successfully");
    }

    /**
     * Get Peer List
     * Retrieve all devices for current user
     *
     * @param httpRequest HTTP request
     * @return List of peers
     */
    @GetMapping("/peers")
    @Operation(summary = "Get Peer List", description = "Get all devices for current user")
    public ApiResponse<List<PeerResponse>> getPeers(HttpServletRequest httpRequest) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            log.warn("Failed to get peers: No valid token");
            return ApiResponse.unauthorized("Authentication required");
        }

        List<Peer> peers = peerService.findByUserId(userId);
        List<PeerResponse> peerResponses = peers.stream()
                .map(this::convertToPeerResponse)
                .collect(Collectors.toList());

        log.debug("Retrieved {} peers for user ID: {}", peerResponses.size(), userId);
        return ApiResponse.success(peerResponses);
    }

    /**
     * Convert Peer entity to PeerResponse DTO
     *
     * @param peer Peer entity
     * @return PeerResponse DTO
     */
    private PeerResponse convertToPeerResponse(Peer peer) {
        // Check if device is online (within last 5 minutes)
        long fiveMinutesAgo = System.currentTimeMillis() - (5 * 60 * 1000);
        boolean online = peer.getLastOnlineTime() != null &&
                peer.getLastOnlineTime() > fiveMinutesAgo;

        return PeerResponse.builder()
                .id(peer.getId())
                .deviceId(peer.getDeviceId())
                .cpu(peer.getCpu())
                .hostname(peer.getHostname())
                .memory(peer.getMemory())
                .os(peer.getOs())
                .username(peer.getUsername())
                .uuid(peer.getUuid())
                .version(peer.getVersion())
                .userId(peer.getUserId())
                .lastOnlineTime(peer.getLastOnlineTime())
                .lastOnlineIp(peer.getLastOnlineIp())
                .groupId(peer.getGroupId())
                .alias(peer.getAlias())
                .online(online)
                .build();
    }

    /**
     * Get current user ID from SecurityContext
     *
     * @return User ID or null
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            return user.getId();
        }
        return null;
    }
}

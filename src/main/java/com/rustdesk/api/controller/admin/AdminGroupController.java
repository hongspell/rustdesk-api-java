package com.rustdesk.api.controller.admin;

import com.rustdesk.api.dto.response.ApiResponse;
import com.rustdesk.api.entity.Group;
import com.rustdesk.api.entity.User;
import com.rustdesk.api.exception.ApiException;
import com.rustdesk.api.service.GroupService;
import com.rustdesk.api.service.UserService;
import com.rustdesk.api.util.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Admin Group Controller
 * Handles group management operations for administrators
 *
 * @author RustDesk
 * @version 2.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Validated
@Tag(name = "Admin Group Management", description = "Admin Group Management API")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminGroupController {

    private final GroupService groupService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Get Group List
     *
     * @param httpRequest HTTP request
     * @return List of all groups
     */
    @GetMapping("/group/list")
    @Operation(summary = "Get Group List", description = "Get list of all groups")
    public ApiResponse<List<Group>> getGroupList(HttpServletRequest httpRequest) {
        if (!isAdmin(httpRequest)) {
            log.warn("Failed to get group list: User is not admin");
            return ApiResponse.forbidden("Admin permission required");
        }

        List<Group> groups = groupService.findAll();

        log.debug("Retrieved {} groups", groups.size());
        return ApiResponse.success(groups);
    }

    /**
     * Create Group
     *
     * @param request Group create request
     * @param httpRequest HTTP request
     * @return Newly created group
     */
    @PostMapping("/group/create")
    @Operation(summary = "Create Group", description = "Create a new group")
    public ApiResponse<Group> createGroup(
            @RequestBody Map<String, Object> request,
            HttpServletRequest httpRequest) {
        if (!isAdmin(httpRequest)) {
            log.warn("Failed to create group: User is not admin");
            return ApiResponse.forbidden("Admin permission required");
        }

        String name = (String) request.get("name");
        Integer type = (Integer) request.get("type");

        log.info("Creating group: {}", name);

        // Create new group
        Group group = new Group();
        group.setName(name);
        group.setType(type != null ? type : 1);

        Group savedGroup = groupService.createGroup(group);

        log.info("Group created successfully: {}", name);
        return ApiResponse.success("Group created successfully", savedGroup);
    }

    /**
     * Update Group
     *
     * @param request Group update request
     * @param httpRequest HTTP request
     * @return Updated group
     */
    @PostMapping("/group/update")
    @Operation(summary = "Update Group", description = "Update an existing group")
    public ApiResponse<Group> updateGroup(
            @RequestBody Map<String, Object> request,
            HttpServletRequest httpRequest) {
        if (!isAdmin(httpRequest)) {
            log.warn("Failed to update group: User is not admin");
            return ApiResponse.forbidden("Admin permission required");
        }

        Long groupId = Long.valueOf(request.get("id").toString());
        log.info("Updating group ID: {}", groupId);

        Group group = groupService.findById(groupId)
                .orElseThrow(() -> ApiException.notFound("Group not found"));

        // Update fields if present
        if (request.containsKey("name")) {
            group.setName((String) request.get("name"));
        }
        if (request.containsKey("type")) {
            group.setType((Integer) request.get("type"));
        }

        Group updatedGroup = groupService.updateGroup(group);

        log.info("Group updated successfully: {}", group.getName());
        return ApiResponse.success("Group updated successfully", updatedGroup);
    }

    /**
     * Delete Group
     *
     * @param request Delete request with group ID
     * @param httpRequest HTTP request
     * @return Success response
     */
    @PostMapping("/group/delete")
    @Operation(summary = "Delete Group", description = "Delete a group")
    public ApiResponse<Void> deleteGroup(
            @RequestBody Map<String, Long> request,
            HttpServletRequest httpRequest) {
        if (!isAdmin(httpRequest)) {
            log.warn("Failed to delete group: User is not admin");
            return ApiResponse.forbidden("Admin permission required");
        }

        Long groupId = request.get("id");
        log.info("Deleting group ID: {}", groupId);

        Group group = groupService.findById(groupId)
                .orElseThrow(() -> ApiException.notFound("Group not found"));

        groupService.deleteGroup(groupId);

        log.info("Group deleted successfully: {}", group.getName());
        return ApiResponse.success("Group deleted successfully");
    }

    /**
     * Check if current user is admin
     *
     * @param request HTTP request
     * @return true if admin, false otherwise
     */
    private boolean isAdmin(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        if (userId == null) {
            return false;
        }

        return userService.findById(userId)
                .map(User::getIsAdmin)
                .orElse(false);
    }

    /**
     * Extract current user ID from JWT token
     *
     * @param request HTTP request
     * @return User ID or null
     */
    private Long getCurrentUserId(HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null) {
            return null;
        }
        try {
            return jwtTokenProvider.getUserIdFromToken(token);
        } catch (Exception e) {
            log.error("Failed to extract user ID from token", e);
            return null;
        }
    }

    /**
     * Extract token from request header
     *
     * @param request HTTP request
     * @return Token string or null
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

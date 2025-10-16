package com.rustdesk.api.controller.admin;

import com.rustdesk.api.dto.request.UserCreateRequest;
import com.rustdesk.api.dto.response.ApiResponse;
import com.rustdesk.api.dto.response.UserResponse;
import com.rustdesk.api.entity.User;
import com.rustdesk.api.exception.ApiException;
import com.rustdesk.api.service.UserService;
import com.rustdesk.api.util.JwtTokenProvider;
import com.rustdesk.api.util.PasswordUtil;
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
import java.util.stream.Collectors;

/**
 * Admin User Controller
 * Handles user management operations for administrators
 *
 * @author RustDesk
 * @version 2.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Validated
@Tag(name = "Admin User Management", description = "Admin User Management API")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminUserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Get Current Admin Info
     *
     * @param httpRequest HTTP request
     * @return Current admin information
     */
    @GetMapping("/user/current")
    @Operation(summary = "Get Current Admin", description = "Get authenticated admin's information")
    public ApiResponse<UserResponse> getCurrentAdmin(HttpServletRequest httpRequest) {
        Long userId = getCurrentUserId(httpRequest);
        if (userId == null) {
            log.warn("Failed to get admin info: No valid token");
            return ApiResponse.unauthorized("Authentication required");
        }

        User user = userService.findById(userId)
                .orElseThrow(() -> ApiException.notFound("User not found"));

        if (!user.getIsAdmin()) {
            log.warn("Failed to get admin info: User is not admin - {}", user.getUsername());
            return ApiResponse.forbidden("Admin permission required");
        }

        UserResponse userResponse = convertToUserResponse(user);

        log.debug("Retrieved admin info for user: {}", user.getUsername());
        return ApiResponse.success(userResponse);
    }

    /**
     * Get User List
     *
     * @param httpRequest HTTP request
     * @return List of all users
     */
    @GetMapping("/user/list")
    @Operation(summary = "Get User List", description = "Get list of all users")
    public ApiResponse<List<UserResponse>> getUserList(HttpServletRequest httpRequest) {
        if (!isAdmin(httpRequest)) {
            log.warn("Failed to get user list: User is not admin");
            return ApiResponse.forbidden("Admin permission required");
        }

        List<User> users = userService.findAll();
        List<UserResponse> userResponses = users.stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());

        log.debug("Retrieved {} users", userResponses.size());
        return ApiResponse.success(userResponses);
    }

    /**
     * Create User
     *
     * @param request User create request
     * @param httpRequest HTTP request
     * @return Newly created user
     */
    @PostMapping("/user/create")
    @Operation(summary = "Create User", description = "Create a new user")
    public ApiResponse<UserResponse> createUser(
            @Validated @RequestBody UserCreateRequest request,
            HttpServletRequest httpRequest) {
        if (!isAdmin(httpRequest)) {
            log.warn("Failed to create user: User is not admin");
            return ApiResponse.forbidden("Admin permission required");
        }

        log.info("Creating user: {}", request.getUsername());

        // Check if username already exists
        if (userService.findByUsername(request.getUsername()).isPresent()) {
            log.warn("Failed to create user: Username already exists - {}", request.getUsername());
            return ApiResponse.conflict("Username already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(PasswordUtil.encryptPassword(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setGroupId(request.getGroupId());
        user.setIsAdmin(request.getIsAdmin());
        user.setStatus(request.getStatus());

        User savedUser = userService.createUser(user);
        UserResponse userResponse = convertToUserResponse(savedUser);

        log.info("User created successfully: {}", request.getUsername());
        return ApiResponse.success("User created successfully", userResponse);
    }

    /**
     * Update User
     *
     * @param request User update request
     * @param httpRequest HTTP request
     * @return Updated user
     */
    @PostMapping("/user/update")
    @Operation(summary = "Update User", description = "Update an existing user")
    public ApiResponse<UserResponse> updateUser(
            @RequestBody Map<String, Object> request,
            HttpServletRequest httpRequest) {
        if (!isAdmin(httpRequest)) {
            log.warn("Failed to update user: User is not admin");
            return ApiResponse.forbidden("Admin permission required");
        }

        Long userId = Long.valueOf(request.get("id").toString());
        log.info("Updating user ID: {}", userId);

        User user = userService.findById(userId)
                .orElseThrow(() -> ApiException.notFound("User not found"));

        // Update fields if present
        if (request.containsKey("username")) {
            user.setUsername((String) request.get("username"));
        }
        if (request.containsKey("email")) {
            user.setEmail((String) request.get("email"));
        }
        if (request.containsKey("password")) {
            user.setPassword(PasswordUtil.encryptPassword((String) request.get("password")));
        }
        if (request.containsKey("nickname")) {
            user.setNickname((String) request.get("nickname"));
        }
        if (request.containsKey("groupId")) {
            user.setGroupId(Long.valueOf(request.get("groupId").toString()));
        }
        if (request.containsKey("isAdmin")) {
            user.setIsAdmin((Boolean) request.get("isAdmin"));
        }
        if (request.containsKey("status")) {
            user.setStatus((Integer) request.get("status"));
        }
        if (request.containsKey("remark")) {
            user.setRemark((String) request.get("remark"));
        }

        User updatedUser = userService.updateUser(user);
        UserResponse userResponse = convertToUserResponse(updatedUser);

        log.info("User updated successfully: {}", user.getUsername());
        return ApiResponse.success("User updated successfully", userResponse);
    }

    /**
     * Delete User
     *
     * @param request Delete request with user ID
     * @param httpRequest HTTP request
     * @return Success response
     */
    @PostMapping("/user/delete")
    @Operation(summary = "Delete User", description = "Delete a user")
    public ApiResponse<Void> deleteUser(
            @RequestBody Map<String, Long> request,
            HttpServletRequest httpRequest) {
        if (!isAdmin(httpRequest)) {
            log.warn("Failed to delete user: User is not admin");
            return ApiResponse.forbidden("Admin permission required");
        }

        Long userId = request.get("id");
        log.info("Deleting user ID: {}", userId);

        User user = userService.findById(userId)
                .orElseThrow(() -> ApiException.notFound("User not found"));

        userService.deleteUser(userId);

        log.info("User deleted successfully: {}", user.getUsername());
        return ApiResponse.success("User deleted successfully");
    }

    /**
     * Convert User entity to UserResponse DTO
     *
     * @param user User entity
     * @return UserResponse DTO
     */
    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .groupId(user.getGroupId())
                .isAdmin(user.getIsAdmin())
                .status(user.getStatus())
                .remark(user.getRemark())
                .build();
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

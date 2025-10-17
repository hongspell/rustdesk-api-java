package com.rustdesk.api.controller.api;

import cn.dev33.satoken.stp.StpUtil;
import com.rustdesk.api.dto.request.ChangePasswordRequest;
import com.rustdesk.api.dto.response.ApiResponse;
import com.rustdesk.api.dto.response.UserResponse;
import com.rustdesk.api.entity.User;
import com.rustdesk.api.exception.ApiException;
import com.rustdesk.api.service.UserService;
import com.rustdesk.api.util.PasswordUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * User Controller
 * Handles user-related operations
 *
 * @author RustDesk
 * @version 2.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
@Tag(name = "User", description = "User API")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;

    /**
     * Get Current User Info
     *
     * @param httpRequest HTTP request
     * @return Current user information
     */
    @GetMapping("/user/info")
    @Operation(summary = "Get Current User Info", description = "Get authenticated user's information")
    public ApiResponse<UserResponse> getUserInfo(HttpServletRequest httpRequest) {
        Long userId = getCurrentUserId(httpRequest);
        if (userId == null) {
            log.warn("Failed to get user info: No valid token");
            return ApiResponse.unauthorized("Authentication required");
        }

        User user = userService.findById(userId)
                .orElseThrow(() -> ApiException.notFound("User not found"));

        UserResponse userResponse = UserResponse.builder()
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

        log.debug("Retrieved user info for user: {}", user.getUsername());
        return ApiResponse.success(userResponse);
    }

    /**
     * Get Current User (Compatible endpoint)
     *
     * @param httpRequest HTTP request
     * @return Current user information
     */
    @PostMapping("/currentUser")
    @Operation(summary = "Get Current User", description = "Get authenticated user's information (compatible endpoint)")
    public ApiResponse<UserResponse> getCurrentUser(HttpServletRequest httpRequest) {
        return getUserInfo(httpRequest);
    }

    /**
     * Change Password
     *
     * @param request Change password request
     * @param httpRequest HTTP request
     * @return Success response
     */
    @PostMapping("/user/changePassword")
    @Operation(summary = "Change Password", description = "Change current user's password")
    public ApiResponse<Void> changePassword(
            @Validated @RequestBody ChangePasswordRequest request,
            HttpServletRequest httpRequest) {
        Long userId = getCurrentUserId(httpRequest);
        if (userId == null) {
            log.warn("Failed to change password: No valid token");
            return ApiResponse.unauthorized("Authentication required");
        }

        User user = userService.findById(userId)
                .orElseThrow(() -> ApiException.notFound("User not found"));

        // Verify old password
        if (!PasswordUtil.verifyPassword(request.getOldPassword(), user.getPassword())) {
            log.warn("Failed to change password: Invalid old password for user - {}", user.getUsername());
            return ApiResponse.badRequest("Invalid old password");
        }

        // Update password
        String newPasswordHash = PasswordUtil.encryptPassword(request.getNewPassword());
        user.setPassword(newPasswordHash);
        userService.updateUser(user);

        log.info("Password changed successfully for user: {}", user.getUsername());
        return ApiResponse.success("Password changed successfully");
    }

    /**
     * Get current user ID from Sa-Token
     *
     * @param request HTTP request
     * @return User ID or null
     */
    private Long getCurrentUserId(HttpServletRequest request) {
        try {
            return StpUtil.getLoginIdAsLong();
        } catch (Exception e) {
            return null;
        }
    }
}

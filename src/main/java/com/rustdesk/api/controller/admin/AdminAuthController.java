package com.rustdesk.api.controller.admin;

import cn.dev33.satoken.stp.StpUtil;
import com.rustdesk.api.dto.request.LoginRequest;
import com.rustdesk.api.dto.request.RegisterRequest;
import com.rustdesk.api.dto.response.ApiResponse;
import com.rustdesk.api.dto.response.LoginResponse;
import com.rustdesk.api.dto.response.UserResponse;
import com.rustdesk.api.entity.User;
import com.rustdesk.api.entity.UserToken;
import com.rustdesk.api.service.LoginLogService;
import com.rustdesk.api.service.UserService;
import com.rustdesk.api.service.UserTokenService;
import com.rustdesk.api.util.PasswordUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Admin Authentication Controller
 * Handles admin authentication operations
 *
 * @author RustDesk
 * @version 2.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Validated
@Tag(name = "Admin Authentication", description = "Admin Authentication API")
public class AdminAuthController {

    private final UserService userService;
    private final UserTokenService userTokenService;
    private final LoginLogService loginLogService;

    /**
     * Admin Login
     *
     * @param request Login request
     * @param httpRequest HTTP request
     * @return Login response with token
     */
    @PostMapping("/login")
    @Operation(summary = "Admin Login", description = "Authenticate admin user and return access token")
    public ApiResponse<LoginResponse> login(
            @Validated @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        log.info("Admin login attempt for username: {}", request.getUsername());

        // Find user by username
        User user = userService.findByUsername(request.getUsername())
                .orElse(null);
        if (user == null) {
            log.warn("Admin login failed: User not found - {}", request.getUsername());
            return ApiResponse.unauthorized("Invalid username or password");
        }

        // Verify admin permission
        if (!user.getIsAdmin()) {
            log.warn("Admin login failed: User is not admin - {}", request.getUsername());
            return ApiResponse.forbidden("Access denied: Admin permission required");
        }

        // Verify password
        if (!PasswordUtil.verifyPassword(request.getPassword(), user.getPassword())) {
            log.warn("Admin login failed: Invalid password for user - {}", request.getUsername());
            return ApiResponse.unauthorized("Invalid username or password");
        }

        // Check user status
        if (user.getStatus() != 1) {
            log.warn("Admin login failed: User is inactive - {}", request.getUsername());
            return ApiResponse.forbidden("User account is inactive");
        }

        // Use Sa-Token for login
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();
        Long expiredAt = System.currentTimeMillis() + (StpUtil.getTokenTimeout() * 1000);

        // Save token to database for compatibility
        UserToken userToken = userTokenService.createToken(user, request.getDeviceId(), request.getDeviceUuid());

        // Log login
        loginLogService.createLog(user.getId(), "webadmin", "account",
                httpRequest.getRemoteAddr(), request.getDeviceId(), request.getDeviceUuid());

        // Build response
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

        LoginResponse loginResponse = LoginResponse.builder()
                .token(token)
                .user(userResponse)
                .expiredAt(expiredAt)
                .build();

        log.info("Admin login successful for user: {}", request.getUsername());
        return ApiResponse.success(loginResponse);
    }

    /**
     * Admin Logout
     *
     * @param httpRequest HTTP request
     * @return Success response
     */
    @PostMapping("/logout")
    @Operation(summary = "Admin Logout", description = "Logout admin user and invalidate token")
    public ApiResponse<Void> logout(HttpServletRequest httpRequest) {
        // Sa-Token logout
        StpUtil.logout();
        log.info("Admin logged out successfully");
        return ApiResponse.success("Logout successful");
    }

    /**
     * User Registration
     *
     * @param request Register request
     * @return Newly created user
     */
    @PostMapping("/user/register")
    @Operation(summary = "User Registration", description = "Register a new user")
    public ApiResponse<UserResponse> register(@Validated @RequestBody RegisterRequest request) {
        log.info("User registration attempt for username: {}", request.getUsername());

        // Check if username already exists
        if (userService.findByUsername(request.getUsername()).isPresent()) {
            log.warn("Registration failed: Username already exists - {}", request.getUsername());
            return ApiResponse.conflict("Username already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(PasswordUtil.encryptPassword(request.getPassword()));
        user.setIsAdmin(false);
        user.setStatus(1);

        User savedUser = userService.createUser(user);

        UserResponse userResponse = UserResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .nickname(savedUser.getNickname())
                .avatar(savedUser.getAvatar())
                .groupId(savedUser.getGroupId())
                .isAdmin(savedUser.getIsAdmin())
                .status(savedUser.getStatus())
                .remark(savedUser.getRemark())
                .build();

        log.info("User registered successfully: {}", request.getUsername());
        return ApiResponse.success("User registered successfully", userResponse);
    }

}

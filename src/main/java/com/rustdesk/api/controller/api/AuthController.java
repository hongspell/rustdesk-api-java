package com.rustdesk.api.controller.api;

import com.rustdesk.api.dto.request.LoginRequest;
import com.rustdesk.api.dto.response.ApiResponse;
import com.rustdesk.api.dto.response.LoginResponse;
import com.rustdesk.api.dto.response.UserResponse;
import com.rustdesk.api.entity.User;
import com.rustdesk.api.entity.UserToken;
import com.rustdesk.api.service.LoginLogService;
import com.rustdesk.api.service.UserService;
import com.rustdesk.api.service.UserTokenService;
import com.rustdesk.api.util.JwtTokenProvider;
import com.rustdesk.api.util.PasswordUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Authentication Controller
 * Handles user authentication operations
 *
 * @author RustDesk
 * @version 2.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    private final UserService userService;
    private final UserTokenService userTokenService;
    private final LoginLogService loginLogService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * User Login
     *
     * @param request Login request
     * @param httpRequest HTTP request
     * @return Login response with token
     */
    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticate user and return access token")
    public ApiResponse<LoginResponse> login(
            @Validated @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        log.info("Login attempt for username: {}", request.getUsername());

        // Find user by username
        User user = userService.findByUsername(request.getUsername())
                .orElse(null);
        if (user == null) {
            log.warn("Login failed: User not found - {}", request.getUsername());
            return ApiResponse.unauthorized("Invalid username or password");
        }

        // Verify password
        if (!PasswordUtil.verifyPassword(request.getPassword(), user.getPassword())) {
            log.warn("Login failed: Invalid password for user - {}", request.getUsername());
            return ApiResponse.unauthorized("Invalid username or password");
        }

        // Check user status
        if (user.getStatus() != 1) {
            log.warn("Login failed: User is inactive - {}", request.getUsername());
            return ApiResponse.forbidden("User account is inactive");
        }

        // Save token to database (use MD5 token from UserTokenService)
        UserToken userToken = userTokenService.createToken(user, request.getDeviceId(), request.getDeviceUuid());
        String token = userToken.getToken();
        Long expiredAt = userToken.getExpiredAt();

        // Log login
        loginLogService.createLog(user.getId(), "webclient", "account",
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

        log.info("Login successful for user: {}", request.getUsername());
        return ApiResponse.success(loginResponse);
    }

    /**
     * User Logout
     *
     * @param httpRequest HTTP request
     * @return Success response
     */
    @PostMapping("/logout")
    @Operation(summary = "User Logout", description = "Logout current user and invalidate token")
    public ApiResponse<Void> logout(HttpServletRequest httpRequest) {
        String token = extractToken(httpRequest);
        if (token != null) {
            userTokenService.deleteToken(token);
            log.info("User logged out successfully");
        }
        return ApiResponse.success("Logout successful");
    }

    /**
     * Get Login Options
     *
     * @return Login options configuration
     */
    @GetMapping("/login-options")
    @Operation(summary = "Get Login Options", description = "Get login configuration options")
    public ApiResponse<Map<String, Object>> getLoginOptions() {
        log.debug("Fetching login options");
        Map<String, Object> options = new HashMap<>();
        options.put("enableRegistration", true);
        options.put("enableOAuth", false);
        options.put("enableLDAP", false);
        return ApiResponse.success(options);
    }

    /**
     * Heartbeat
     *
     * @return Success response
     */
    @PostMapping("/heartbeat")
    @Operation(summary = "Heartbeat", description = "Keep-alive heartbeat check")
    public ApiResponse<Map<String, Object>> heartbeat() {
        log.debug("Heartbeat received");
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", "ok");
        return ApiResponse.success(response);
    }

    /**
     * Get Version Information
     *
     * @return Version information
     */
    @GetMapping("/version")
    @Operation(summary = "Get Version", description = "Get API version information")
    public ApiResponse<Map<String, String>> getVersion() {
        log.debug("Fetching version information");
        Map<String, String> version = new HashMap<>();
        version.put("version", "2.0.0");
        version.put("apiVersion", "v1");
        version.put("buildTime", "2024-10-16");
        return ApiResponse.success(version);
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

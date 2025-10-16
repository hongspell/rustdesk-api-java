package com.rustdesk.api.security.filter;

import com.rustdesk.api.entity.User;
import com.rustdesk.api.entity.UserToken;
import com.rustdesk.api.repository.UserRepository;
import com.rustdesk.api.repository.UserTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Admin Authentication Filter
 * <p>
 * Intercepts requests to admin endpoints and validates tokens from the api-token header.
 * Validates tokens against the user_tokens table and checks expiration.
 * </p>
 *
 * @author RustDesk
 * @version 2.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminAuthenticationFilter extends OncePerRequestFilter {

    private final UserTokenRepository userTokenRepository;
    private final UserRepository userRepository;

    private static final String API_TOKEN_HEADER = "api-token";

    /**
     * Filter incoming requests to authenticate admin users via api-token
     *
     * @param request     HTTP request
     * @param response    HTTP response
     * @param filterChain filter chain
     * @throws ServletException if servlet error occurs
     * @throws IOException      if I/O error occurs
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            // Extract token from api-token header
            String token = extractTokenFromRequest(request);

            // Validate token and authenticate user
            if (StringUtils.hasText(token)) {
                UserToken userToken = userTokenRepository.findByToken(token).orElse(null);

                if (userToken != null && isTokenValid(userToken)) {
                    // Load user from database
                    User user = userRepository.findById(userToken.getUserId()).orElse(null);

                    if (user != null && user.getStatus() == 1) {
                        // Verify user has admin privileges
                        if (user.getIsAdmin() != null && user.getIsAdmin()) {
                            // Create authentication token
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(
                                            user,
                                            null,
                                            getAuthorities(user)
                                    );

                            authentication.setDetails(
                                    new WebAuthenticationDetailsSource().buildDetails(request)
                            );

                            // Set authentication in security context
                            SecurityContextHolder.getContext().setAuthentication(authentication);

                            log.debug("Authenticated admin user: {} (ID: {})", user.getUsername(), user.getId());
                        } else {
                            log.warn("User {} is not an admin", user.getUsername());
                        }
                    } else {
                        log.warn("User not found or inactive for user ID: {}", userToken.getUserId());
                    }
                } else {
                    if (userToken != null) {
                        log.debug("Token expired or invalid");
                    }
                }
            }
        } catch (Exception e) {
            log.error("Cannot set admin user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extract token from api-token header
     *
     * @param request HTTP request
     * @return token string or null if not found
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(API_TOKEN_HEADER);

        if (StringUtils.hasText(token)) {
            return token.trim();
        }

        return null;
    }

    /**
     * Check if token is valid (not expired)
     *
     * @param userToken user token entity
     * @return true if token is valid
     */
    private boolean isTokenValid(UserToken userToken) {
        if (userToken.getExpiredAt() == null) {
            return false;
        }

        Long now = System.currentTimeMillis();
        return userToken.getExpiredAt() > now;
    }

    /**
     * Get authorities (roles) for user
     *
     * @param user user entity
     * @return list of granted authorities
     */
    private List<SimpleGrantedAuthority> getAuthorities(User user) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (user.getIsAdmin() != null && user.getIsAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return authorities;
    }
}

package com.rustdesk.api.security.filter;

import com.rustdesk.api.entity.User;
import com.rustdesk.api.entity.UserToken;
import com.rustdesk.api.repository.UserRepository;
import com.rustdesk.api.service.UserTokenService;
import com.rustdesk.api.util.JwtTokenProvider;
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
import java.util.Optional;

/**
 * JWT Authentication Filter
 * <p>
 * Intercepts requests to extract and validate JWT tokens from the Authorization header.
 * If a valid token is found, loads the user and sets up Spring Security authentication.
 * </p>
 *
 * @author RustDesk
 * @version 2.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserTokenService userTokenService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * Filter incoming requests to authenticate via JWT token
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
            // Extract token from Authorization header
            String token = extractJwtFromRequest(request);

            if (StringUtils.hasText(token)) {
                User user = null;

                // Try JWT validation first if JWT is enabled
                if (jwtTokenProvider.isJwtEnabled() && jwtTokenProvider.validateToken(token)) {
                    Long userId = jwtTokenProvider.getUserIdFromToken(token);
                    user = userRepository.findById(userId).orElse(null);
                    log.debug("Validated JWT token for user ID: {}", userId);
                } else {
                    // Try MD5 token validation from database
                    if (userTokenService.validateToken(token)) {
                        Optional<UserToken> userTokenOpt = userTokenService.findByToken(token);
                        if (userTokenOpt.isPresent()) {
                            UserToken userToken = userTokenOpt.get();
                            user = userRepository.findById(userToken.getUserId()).orElse(null);
                            log.debug("Validated MD5 token for user ID: {}", userToken.getUserId());
                        }
                    } else {
                        log.debug("Invalid or expired token");
                    }
                }

                // If user found and active, set authentication
                if (user != null && user.getStatus() == 1) {
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

                    log.debug("Authenticated user: {} (ID: {})", user.getUsername(), user.getId());
                } else if (user != null) {
                    log.warn("User is inactive: {}", user.getUsername());
                }
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extract JWT token from Authorization header
     *
     * @param request HTTP request
     * @return JWT token string or null if not found
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
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

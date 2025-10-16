package com.rustdesk.api.config;

import com.rustdesk.api.security.filter.AdminAuthenticationFilter;
import com.rustdesk.api.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security Configuration
 * <p>
 * Configures security for both RustDesk client API and admin web interface.
 * Uses JWT-based authentication with stateless sessions.
 * </p>
 *
 * @author RustDesk
 * @version 2.0.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AdminAuthenticationFilter adminAuthenticationFilter;

    /**
     * Security filter chain for admin endpoints
     * <p>
     * Handles /api/admin/** paths with admin-specific authentication.
     * Uses custom admin token authentication via api-token header.
     * </p>
     *
     * @param http HttpSecurity configuration
     * @return configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/admin/**")
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public admin endpoints
                        .requestMatchers(
                                "/api/admin/login",
                                "/api/admin/captcha",
                                "/api/admin/register"
                        ).permitAll()
                        // All other admin endpoints require authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(adminAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Security filter chain for client API endpoints
     * <p>
     * Handles /api/** paths (excluding /api/admin/**) with JWT authentication.
     * Provides public access to certain endpoints like login, OAuth, and system info.
     * </p>
     *
     * @param http HttpSecurity configuration
     * @return configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints for RustDesk client
                        .requestMatchers(
                                "/api/login",
                                "/api/login-options",
                                "/api/oauth/**",
                                "/api/sysinfo",
                                "/api/heartbeat",
                                "/api/health"
                        ).permitAll()
                        // API documentation endpoints
                        .requestMatchers(
                                "/api/swagger-ui/**",
                                "/api/v3/api-docs/**",
                                "/api/swagger-resources/**"
                        ).permitAll()
                        // All other API endpoints require authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Default security filter chain
     * <p>
     * Handles all other paths not matched by admin or API filter chains.
     * Permits all requests by default.
     * </p>
     *
     * @param http HttpSecurity configuration
     * @return configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    @Order(3)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    /**
     * Password encoder bean
     * Uses BCrypt for password hashing
     *
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

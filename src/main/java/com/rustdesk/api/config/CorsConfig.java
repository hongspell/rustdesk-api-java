package com.rustdesk.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * CORS (Cross-Origin Resource Sharing) Configuration
 * <p>
 * Configures CORS settings to allow cross-origin requests from web clients.
 * Allows all origins, methods, and headers for maximum compatibility.
 * </p>
 *
 * @author RustDesk
 * @version 2.0.0
 */
@Configuration
public class CorsConfig {

    /**
     * Configure CORS settings
     * <p>
     * Allows:
     * - All origins (*)
     * - All HTTP methods (GET, POST, PUT, DELETE, OPTIONS, etc.)
     * - All headers
     * - Credentials (cookies, authorization headers)
     * - Max age of 3600 seconds for preflight cache
     * </p>
     *
     * @return CORS configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow all origins
        configuration.setAllowedOriginPatterns(List.of("*"));

        // Allow all HTTP methods
        configuration.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS",
                "HEAD"
        ));

        // Allow all headers
        configuration.setAllowedHeaders(List.of("*"));

        // Expose common headers to client
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers",
                "api-token"
        ));

        // Allow credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);

        // How long the response from a pre-flight request can be cached (1 hour)
        configuration.setMaxAge(3600L);

        // Apply CORS configuration to all paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}

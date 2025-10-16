package com.rustdesk.api.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * RustDesk Configuration Properties
 * Maps to application.yml rustdesk configuration section
 */
@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "rustdesk")
public class RustDeskProperties {

    /**
     * Server configuration
     */
    private Server server = new Server();

    /**
     * Security configuration
     */
    private Security security = new Security();

    /**
     * Token configuration
     */
    private Token token = new Token();

    /**
     * File upload configuration
     */
    private Upload upload = new Upload();

    /**
     * ID Server configuration
     */
    private IdServer idServer = new IdServer();

    /**
     * Relay Server configuration
     */
    private RelayServer relayServer = new RelayServer();

    @Data
    public static class Server {
        /**
         * Server host
         */
        @NotBlank(message = "Server host cannot be blank")
        private String host = "0.0.0.0";

        /**
         * Server port
         */
        @NotNull(message = "Server port cannot be null")
        @Min(value = 1, message = "Server port must be greater than 0")
        private Integer port = 21114;

        /**
         * Enable HTTPS
         */
        private Boolean https = false;

        /**
         * Base URL for the application
         */
        private String baseUrl = "http://localhost:21114";
    }

    @Data
    public static class Security {
        /**
         * JWT secret key
         */
        @NotBlank(message = "JWT secret cannot be blank")
        private String jwtSecret = "rustdesk-secret-key-change-in-production";

        /**
         * Enable CORS
         */
        private Boolean corsEnabled = true;

        /**
         * Allowed origins for CORS
         */
        private String[] allowedOrigins = {"*"};

        /**
         * Enable captcha verification
         */
        private Boolean captchaEnabled = false;

        /**
         * Maximum login attempts before lockout
         */
        @Min(value = 1, message = "Max login attempts must be greater than 0")
        private Integer maxLoginAttempts = 5;

        /**
         * Account lockout duration in minutes
         */
        @Min(value = 1, message = "Lockout duration must be greater than 0")
        private Integer lockoutDuration = 30;
    }

    @Data
    public static class Token {
        /**
         * Token expiration time in seconds
         */
        @NotNull(message = "Token expiration cannot be null")
        @Min(value = 60, message = "Token expiration must be at least 60 seconds")
        private Long expiration = 7 * 24 * 60 * 60L; // 7 days

        /**
         * Refresh token expiration in seconds
         */
        @Min(value = 60, message = "Refresh token expiration must be at least 60 seconds")
        private Long refreshExpiration = 30 * 24 * 60 * 60L; // 30 days

        /**
         * Token issuer
         */
        private String issuer = "rustdesk-api";

        /**
         * Allow multiple sessions per user
         */
        private Boolean multipleSession = true;
    }

    @Data
    public static class Upload {
        /**
         * Maximum file size in bytes
         */
        @Min(value = 1, message = "Max file size must be greater than 0")
        private Long maxFileSize = 10 * 1024 * 1024L; // 10MB

        /**
         * Upload directory path
         */
        @NotBlank(message = "Upload path cannot be blank")
        private String path = "./uploads";

        /**
         * Allowed file types
         */
        private String[] allowedTypes = {"image/jpeg", "image/png", "image/gif"};

        /**
         * Enable file upload
         */
        private Boolean enabled = true;
    }

    @Data
    public static class IdServer {
        /**
         * ID Server host
         */
        private String host = "localhost";

        /**
         * ID Server port
         */
        @Min(value = 1, message = "ID Server port must be greater than 0")
        private Integer port = 21116;

        /**
         * ID Server key
         */
        private String key = "";

        /**
         * Enable ID Server integration
         */
        private Boolean enabled = false;
    }

    @Data
    public static class RelayServer {
        /**
         * Relay Server host
         */
        private String host = "localhost";

        /**
         * Relay Server port
         */
        @Min(value = 1, message = "Relay Server port must be greater than 0")
        private Integer port = 21117;

        /**
         * Relay Server key
         */
        private String key = "";

        /**
         * Enable Relay Server integration
         */
        private Boolean enabled = false;
    }
}

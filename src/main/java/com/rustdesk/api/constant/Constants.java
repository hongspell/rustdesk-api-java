package com.rustdesk.api.constant;

/**
 * Constants definition
 */
public final class Constants {

    private Constants() {
        throw new IllegalStateException("Constants class");
    }

    // Authentication Constants
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String TOKEN_ATTRIBUTE = "token";
    public static final String USER_ID_ATTRIBUTE = "userId";

    // Date Time Format
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm:ss";

    // Default Values
    public static final Integer DEFAULT_PAGE_SIZE = 20;
    public static final Integer DEFAULT_PAGE_NUM = 1;
    public static final Integer MAX_PAGE_SIZE = 100;

    // Status
    public static final Integer STATUS_ENABLE = 1;
    public static final Integer STATUS_DISABLED = 2;

    // Group Types
    public static final Integer GROUP_TYPE_USER = 1;
    public static final Integer GROUP_TYPE_DEVICE = 2;

    // Cache Keys
    public static final String CACHE_TOKEN_PREFIX = "token:";
    public static final String CACHE_USER_PREFIX = "user:";
    public static final String CACHE_CAPTCHA_PREFIX = "captcha:";

    // Token Expiration (in seconds)
    public static final Long TOKEN_EXPIRATION = 7 * 24 * 60 * 60L; // 7 days
    public static final Long CAPTCHA_EXPIRATION = 5 * 60L; // 5 minutes

    // Response Messages
    public static final String SUCCESS = "Success";
    public static final String FAILED = "Failed";
    public static final String UNAUTHORIZED = "Unauthorized";
    public static final String FORBIDDEN = "Forbidden";
    public static final String NOT_FOUND = "Not Found";
    public static final String INTERNAL_ERROR = "Internal Server Error";

    // Validation Messages
    public static final String INVALID_PARAMETER = "Invalid parameter";
    public static final String MISSING_PARAMETER = "Missing required parameter";
    public static final String DUPLICATE_ENTRY = "Duplicate entry";

    // RustDesk Specific
    public static final String RUSTDESK_ID_SERVER = "id-server";
    public static final String RUSTDESK_RELAY_SERVER = "relay-server";
    public static final String RUSTDESK_API_SERVER = "api-server";

    // File Upload
    public static final Long MAX_FILE_SIZE = 10 * 1024 * 1024L; // 10MB
    public static final String[] ALLOWED_IMAGE_TYPES = {"image/jpeg", "image/png", "image/gif"};

    // Regular Expressions
    public static final String REGEX_USERNAME = "^[a-zA-Z0-9_-]{3,50}$";
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final String REGEX_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,}$";
}

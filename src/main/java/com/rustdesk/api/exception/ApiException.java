package com.rustdesk.api.exception;

import lombok.Getter;

/**
 * Custom API Exception
 * <p>
 * Base exception class for all API-related errors.
 * Contains error code and message for standardized error responses.
 * </p>
 *
 * @author RustDesk
 * @version 2.0.0
 */
@Getter
public class ApiException extends RuntimeException {

    /**
     * Error code
     */
    private final int code;

    /**
     * Error message
     */
    private final String message;

    /**
     * Constructor with error code and message
     *
     * @param code    error code
     * @param message error message
     */
    public ApiException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * Constructor with message only (uses default code 500)
     *
     * @param message error message
     */
    public ApiException(String message) {
        this(500, message);
    }

    /**
     * Constructor with message and cause
     *
     * @param message error message
     * @param cause   throwable cause
     */
    public ApiException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
        this.message = message;
    }

    /**
     * Constructor with error code, message and cause
     *
     * @param code    error code
     * @param message error message
     * @param cause   throwable cause
     */
    public ApiException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    /**
     * Create bad request exception (400)
     *
     * @param message error message
     * @return ApiException with 400 status
     */
    public static ApiException badRequest(String message) {
        return new ApiException(400, message);
    }

    /**
     * Create unauthorized exception (401)
     *
     * @param message error message
     * @return ApiException with 401 status
     */
    public static ApiException unauthorized(String message) {
        return new ApiException(401, message);
    }

    /**
     * Create forbidden exception (403)
     *
     * @param message error message
     * @return ApiException with 403 status
     */
    public static ApiException forbidden(String message) {
        return new ApiException(403, message);
    }

    /**
     * Create not found exception (404)
     *
     * @param message error message
     * @return ApiException with 404 status
     */
    public static ApiException notFound(String message) {
        return new ApiException(404, message);
    }

    /**
     * Create conflict exception (409)
     *
     * @param message error message
     * @return ApiException with 409 status
     */
    public static ApiException conflict(String message) {
        return new ApiException(409, message);
    }

    /**
     * Create internal server error exception (500)
     *
     * @param message error message
     * @return ApiException with 500 status
     */
    public static ApiException internalError(String message) {
        return new ApiException(500, message);
    }
}

package com.rustdesk.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Unified API Response Wrapper
 * <p>
 * Standard response format for all API endpoints.
 * Provides consistent structure for success and error responses.
 * </p>
 *
 * @param <T> type of response data
 * @author RustDesk
 * @version 2.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /**
     * Response code (HTTP status code)
     * 200 = success
     * 400 = bad request
     * 401 = unauthorized
     * 403 = forbidden
     * 404 = not found
     * 500 = internal server error
     */
    private Integer code;

    /**
     * Response message
     */
    private String message;

    /**
     * Response data (null for error responses)
     */
    private T data;

    /**
     * Create success response with data
     *
     * @param data response data
     * @param <T>  data type
     * @return ApiResponse with success status
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Success", data);
    }

    /**
     * Create success response with custom message
     *
     * @param message success message
     * @param data    response data
     * @param <T>     data type
     * @return ApiResponse with success status
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    /**
     * Create success response without data
     *
     * @return ApiResponse with success status and no data
     */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(200, "Success", null);
    }

    /**
     * Create success response with custom message and no data
     *
     * @param message success message
     * @return ApiResponse with success status and no data
     */
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(200, message, null);
    }

    /**
     * Create error response with code and message
     *
     * @param code    error code
     * @param message error message
     * @param <T>     data type
     * @return ApiResponse with error status
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    /**
     * Create error response with code, message and data
     *
     * @param code    error code
     * @param message error message
     * @param data    error details
     * @param <T>     data type
     * @return ApiResponse with error status
     */
    public static <T> ApiResponse<T> error(int code, String message, T data) {
        return new ApiResponse<>(code, message, data);
    }

    /**
     * Create bad request error response (400)
     *
     * @param message error message
     * @param <T>     data type
     * @return ApiResponse with 400 status
     */
    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(400, message, null);
    }

    /**
     * Create unauthorized error response (401)
     *
     * @param message error message
     * @param <T>     data type
     * @return ApiResponse with 401 status
     */
    public static <T> ApiResponse<T> unauthorized(String message) {
        return new ApiResponse<>(401, message, null);
    }

    /**
     * Create forbidden error response (403)
     *
     * @param message error message
     * @param <T>     data type
     * @return ApiResponse with 403 status
     */
    public static <T> ApiResponse<T> forbidden(String message) {
        return new ApiResponse<>(403, message, null);
    }

    /**
     * Create not found error response (404)
     *
     * @param message error message
     * @param <T>     data type
     * @return ApiResponse with 404 status
     */
    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(404, message, null);
    }

    /**
     * Create conflict error response (409)
     *
     * @param message error message
     * @param <T>     data type
     * @return ApiResponse with 409 status
     */
    public static <T> ApiResponse<T> conflict(String message) {
        return new ApiResponse<>(409, message, null);
    }

    /**
     * Create internal server error response (500)
     *
     * @param message error message
     * @param <T>     data type
     * @return ApiResponse with 500 status
     */
    public static <T> ApiResponse<T> internalError(String message) {
        return new ApiResponse<>(500, message, null);
    }

    /**
     * Check if response is successful
     *
     * @return true if code is 200
     */
    public boolean isSuccess() {
        return code != null && code == 200;
    }

    /**
     * Check if response is an error
     *
     * @return true if code is not 200
     */
    public boolean isError() {
        return code == null || code != 200;
    }
}

package com.poopvibe.app.common;

import java.time.Instant;
import java.util.Map;

/**
 * Standard error response returned by REST controllers.
 */
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors
) {
    /**
     * Creates an error response without field-level validation details.
     *
     * @param status HTTP status code
     * @param error short error label
     * @param message human-readable failure reason
     * @param path request path that failed
     * @return error response with a current timestamp
     */
    public static ApiError of(int status, String error, String message, String path) {
        return new ApiError(Instant.now(), status, error, message, path, null);
    }

    /**
     * Creates a validation error response with field-level details.
     *
     * @param status HTTP status code
     * @param error short error label
     * @param message human-readable failure reason
     * @param path request path that failed
     * @param fieldErrors rejected fields keyed by field name
     * @return error response with a current timestamp
     */
    public static ApiError validation(
            int status,
            String error,
            String message,
            String path,
            Map<String, String> fieldErrors
    ) {
        return new ApiError(Instant.now(), status, error, message, path, fieldErrors);
    }
}

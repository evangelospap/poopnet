package com.poopvibe.app.common;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Maps domain and validation failures into stable JSON API errors.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Converts missing resources into HTTP 404 responses.
     *
     * @param exception exception raised by a service or repository lookup
     * @param request current HTTP request
     * @return not-found error response
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiError.of(404, "Not Found", exception.getMessage(), request.getRequestURI()));
    }

    /**
     * Converts domain rule failures into HTTP 400 responses.
     *
     * @param exception exception raised by service validation
     * @param request current HTTP request
     * @return bad-request error response
     */
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiError> handleBusinessRule(BusinessRuleException exception, HttpServletRequest request) {
        return ResponseEntity.badRequest()
                .body(ApiError.of(400, "Bad Request", exception.getMessage(), request.getRequestURI()));
    }

    /**
     * Converts bean validation failures into field-level HTTP 400 responses.
     *
     * @param exception validation exception raised by Spring MVC
     * @param request current HTTP request
     * @return validation error response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        Map<String, String> fields = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                fields.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest()
                .body(ApiError.validation(
                        400,
                        "Validation Failed",
                        "Request body failed validation.",
                        request.getRequestURI(),
                        fields
                ));
    }
}

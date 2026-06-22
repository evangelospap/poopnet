package com.poopvibe.app.common;

/**
 * Raised when a requested domain object does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Creates a not-found exception for the supplied message.
     *
     * @param message explanation of the missing resource
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

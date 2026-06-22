package com.poopvibe.app.common;

/**
 * Raised when a valid request violates a domain rule.
 */
public class BusinessRuleException extends RuntimeException {

    /**
     * Creates a domain-rule exception for the supplied message.
     *
     * @param message explanation of the violated rule
     */
    public BusinessRuleException(String message) {
        super(message);
    }
}

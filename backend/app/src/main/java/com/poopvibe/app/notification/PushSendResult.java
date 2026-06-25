package com.poopvibe.app.notification;

/**
 * Result of one Web Push delivery attempt.
 */
public record PushSendResult(boolean successful, boolean expired, String detail) {
    /**
     * Creates a successful send result.
     *
     * @param detail provider response summary
     * @return successful result
     */
    public static PushSendResult success(String detail) {
        return new PushSendResult(true, false, detail);
    }

    /**
     * Creates a failed send result.
     *
     * @param expired whether the endpoint should be disabled
     * @param detail provider response or exception summary
     * @return failed result
     */
    public static PushSendResult failure(boolean expired, String detail) {
        return new PushSendResult(false, expired, detail);
    }
}

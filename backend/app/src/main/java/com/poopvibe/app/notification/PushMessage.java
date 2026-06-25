package com.poopvibe.app.notification;

/**
 * Payload and metadata sent to a browser push subscription.
 */
public record PushMessage(
        String title,
        String body,
        String url,
        String icon,
        String tag
) {
}

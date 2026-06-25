package com.poopvibe.app.notification;

import com.poopvibe.app.device.Device;

/**
 * Sends one push message to one browser subscription.
 */
public interface WebPushSender {
    /**
     * Attempts to deliver a push message.
     *
     * @param device browser subscription target
     * @param message notification payload
     * @return delivery result
     */
    PushSendResult send(Device device, PushMessage message);
}

package com.poopvibe.app.notification;

/**
 * Runtime VAPID key pair exposed to the frontend and used by the sender.
 */
public record VapidKeyMaterial(String publicKey, String privateKey) {
}

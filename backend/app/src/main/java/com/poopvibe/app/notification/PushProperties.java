package com.poopvibe.app.notification;

import java.util.Optional;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Holds VAPID settings used by the Web Push sender.
 */
@Component
@ConfigurationProperties(prefix = "app.push")
public class PushProperties {
    private String publicKey = "";
    private String privateKey = "";
    private String subject = "mailto:hello@poopvibe.local";
    private int ttlSeconds = 3600;

    /**
     * Returns the configured public VAPID key when one is provided.
     *
     * @return configured public key, or empty for dev-generated keys
     */
    public Optional<String> configuredPublicKey() {
        return Optional.ofNullable(publicKey).map(String::trim).filter(value -> !value.isEmpty());
    }

    /**
     * Returns the configured private VAPID key when one is provided.
     *
     * @return configured private key, or empty for dev-generated keys
     */
    public Optional<String> configuredPrivateKey() {
        return Optional.ofNullable(privateKey).map(String::trim).filter(value -> !value.isEmpty());
    }

    /**
     * Returns the raw public key property value.
     *
     * @return configured public key value
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * Binds the raw public VAPID key property.
     *
     * @param publicKey public key value
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * Returns the raw private key property value.
     *
     * @return configured private key value
     */
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     * Binds the raw private VAPID key property.
     *
     * @param privateKey private key value
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    /**
     * Returns the VAPID subject claim used for push delivery.
     *
     * @return mailto or URL subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Binds the VAPID subject claim.
     *
     * @param subject mailto or URL subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Returns the notification time-to-live in seconds.
     *
     * @return push TTL seconds
     */
    public int getTtlSeconds() {
        return ttlSeconds;
    }

    /**
     * Binds the notification time-to-live in seconds.
     *
     * @param ttlSeconds push TTL seconds
     */
    public void setTtlSeconds(int ttlSeconds) {
        this.ttlSeconds = ttlSeconds;
    }
}

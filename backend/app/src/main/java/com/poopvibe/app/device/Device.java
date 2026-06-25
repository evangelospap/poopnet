package com.poopvibe.app.device;

import com.poopvibe.app.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * Represents one browser or installed PWA device capable of receiving push notifications.
 */
@Entity
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true, length = 2048)
    private String endpoint;

    @Column(nullable = false, length = 255)
    private String p256dhKey;

    @Column(nullable = false, length = 255)
    private String authKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Deviceapp app;

    @Column(length = 500)
    private String userAgent;

    @Column(nullable = false)
    private boolean pushEnabled;

    @Column(nullable = false)
    private Instant lastSeenAt;

    private Instant lastPushSuccessAt;

    private Instant lastPushFailureAt;

    @Column(length = 500)
    private String lastPushFailureReason;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected Device() {
    }

    /**
     * Creates a device registration for a user.
     *
     * @param user owning user
     * @param endpoint browser push service endpoint
     * @param p256dhKey browser public key used to encrypt push payloads
     * @param authKey browser authentication secret used to encrypt push payloads
     * @param app client app
     * @param userAgent optional browser user-agent string
     * @param pushEnabled whether push notifications are enabled
     */
    public Device(
            User user,
            String endpoint,
            String p256dhKey,
            String authKey,
            Deviceapp app,
            String userAgent,
            boolean pushEnabled
    ) {
        this.user = user;
        this.endpoint = endpoint;
        refresh(p256dhKey, authKey, app, userAgent, pushEnabled);
    }

    /**
     * Refreshes mutable device registration fields.
     *
     * @param p256dhKey browser public key used to encrypt push payloads
     * @param authKey browser authentication secret used to encrypt push payloads
     * @param app client app
     * @param userAgent optional browser user-agent string
     * @param pushEnabled whether push notifications are enabled
     */
    public void refresh(String p256dhKey, String authKey, Deviceapp app, String userAgent, boolean pushEnabled) {
        this.p256dhKey = p256dhKey;
        this.authKey = authKey;
        this.app = app;
        this.userAgent = userAgent;
        this.pushEnabled = pushEnabled;
        this.lastSeenAt = Instant.now();
    }

    /**
     * Records that the most recent push delivery attempt succeeded.
     */
    public void markPushSuccess() {
        this.lastPushSuccessAt = Instant.now();
        this.lastPushFailureReason = null;
    }

    /**
     * Records that the most recent push delivery attempt failed.
     *
     * @param reason short operator-facing failure reason
     */
    public void markPushFailure(String reason) {
        this.lastPushFailureAt = Instant.now();
        this.lastPushFailureReason = reason;
    }

    /**
     * Disables push for this browser subscription after the endpoint expires.
     */
    public void disablePush() {
        this.pushEnabled = false;
        this.lastSeenAt = Instant.now();
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getP256dhKey() {
        return p256dhKey;
    }

    public String getAuthKey() {
        return authKey;
    }

    public Deviceapp getapp() {
        return app;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public boolean isPushEnabled() {
        return pushEnabled;
    }

    public Instant getLastSeenAt() {
        return lastSeenAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getLastPushSuccessAt() {
        return lastPushSuccessAt;
    }

    public Instant getLastPushFailureAt() {
        return lastPushFailureAt;
    }

    public String getLastPushFailureReason() {
        return lastPushFailureReason;
    }
}

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

    @Column(nullable = false, unique = true, length = 512)
    private String fcmToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Deviceapp app;

    @Column(nullable = false)
    private boolean pushEnabled;

    @Column(nullable = false)
    private Instant lastSeenAt;

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
     * @param fcmToken FCM token or equivalent browser push token
     * @param app client app
     * @param pushEnabled whether push notifications are enabled
     */
    public Device(User user, String fcmToken, Deviceapp app, boolean pushEnabled) {
        this.user = user;
        this.fcmToken = fcmToken;
        this.app = app;
        this.pushEnabled = pushEnabled;
        this.lastSeenAt = Instant.now();
    }

    /**
     * Refreshes mutable device registration fields.
     *
     * @param app client app
     * @param pushEnabled whether push notifications are enabled
     */
    public void refresh(Deviceapp app, boolean pushEnabled) {
        this.app = app;
        this.pushEnabled = pushEnabled;
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

    public String getFcmToken() {
        return fcmToken;
    }

    public Deviceapp getapp() {
        return app;
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
}

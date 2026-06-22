package com.poopvibe.app.session;

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
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * Core session log containing timing, privacy, mood, comfort, and offline-sync metadata.
 */
@Entity
@Table(name = "poop_sessions")
public class PoopSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID clientId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant startTime;

    @Column(nullable = false)
    private Instant endTime;

    @Column(nullable = false)
    private long durationSeconds;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SessionMood mood;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ComfortLevel comfortLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SessionPrivacy privacy;

    @Column(length = 1000)
    private String note;

    @Column(nullable = false)
    private boolean syncedFromOffline;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected PoopSession() {
    }

    /**
     * Creates a session log and derives its duration from the supplied timestamps.
     *
     * @param clientId frontend-generated ID used for idempotent offline sync
     * @param user owner of the session
     * @param startTime session start time
     * @param endTime session end time
     * @param mood logged mood
     * @param comfortLevel logged comfort level
     * @param privacy visibility setting
     * @param note optional private note
     * @param syncedFromOffline whether the session originated while offline
     */
    public PoopSession(
            UUID clientId,
            User user,
            Instant startTime,
            Instant endTime,
            SessionMood mood,
            ComfortLevel comfortLevel,
            SessionPrivacy privacy,
            String note,
            boolean syncedFromOffline
    ) {
        this.clientId = clientId;
        this.user = user;
        applyUpdate(startTime, endTime, mood, comfortLevel, privacy, note, syncedFromOffline);
    }

    /**
     * Updates mutable session details and recalculates duration.
     *
     * @param startTime session start time
     * @param endTime session end time
     * @param mood logged mood
     * @param comfortLevel logged comfort level
     * @param privacy visibility setting
     * @param note optional private note
     * @param syncedFromOffline whether the session originated while offline
     */
    public void applyUpdate(
            Instant startTime,
            Instant endTime,
            SessionMood mood,
            ComfortLevel comfortLevel,
            SessionPrivacy privacy,
            String note,
            boolean syncedFromOffline
    ) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationSeconds = Duration.between(startTime, endTime).toSeconds();
        this.mood = mood;
        this.comfortLevel = comfortLevel;
        this.privacy = privacy;
        this.note = note;
        this.syncedFromOffline = syncedFromOffline;
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

    public UUID getClientId() {
        return clientId;
    }

    public User getUser() {
        return user;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public long getDurationSeconds() {
        return durationSeconds;
    }

    public SessionMood getMood() {
        return mood;
    }

    public ComfortLevel getComfortLevel() {
        return comfortLevel;
    }

    public SessionPrivacy getPrivacy() {
        return privacy;
    }

    public String getNote() {
        return note;
    }

    public boolean isSyncedFromOffline() {
        return syncedFromOffline;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}

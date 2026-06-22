package com.poopvibe.app.session;

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
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * Media attachment metadata for a session.
 */
@Entity
@Table(name = "session_media")
public class SessionMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private PoopSession session;

    @Column(nullable = false, length = 500)
    private String mediaUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MediaType mediaType;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected SessionMedia() {
    }

    /**
     * Creates media metadata for a session.
     *
     * @param session target session
     * @param mediaUrl externally hosted media URL
     * @param mediaType media category
     */
    public SessionMedia(PoopSession session, String mediaUrl, MediaType mediaType) {
        this.session = session;
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
    }

    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public PoopSession getSession() {
        return session;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

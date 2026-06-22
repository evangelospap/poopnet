package com.poopvibe.app.session;

import com.poopvibe.app.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;

/**
 * Emoji reaction left by a user on a visible session.
 */
@Entity
@Table(
        name = "session_reactions",
        uniqueConstraints = @UniqueConstraint(name = "uk_session_reaction_user", columnNames = {"session_id", "user_id"})
)
public class SessionReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private PoopSession session;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 12)
    private String emoji;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected SessionReaction() {
    }

    /**
     * Creates or replaces a user's emoji reaction for a session.
     *
     * @param session target session
     * @param user reacting user
     * @param emoji emoji payload
     */
    public SessionReaction(PoopSession session, User user, String emoji) {
        this.session = session;
        this.user = user;
        this.emoji = emoji;
    }

    /**
     * Replaces the reaction emoji while keeping the existing row identity.
     *
     * @param emoji emoji payload
     */
    public void updateEmoji(String emoji) {
        this.emoji = emoji;
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

    public User getUser() {
        return user;
    }

    public String getEmoji() {
        return emoji;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

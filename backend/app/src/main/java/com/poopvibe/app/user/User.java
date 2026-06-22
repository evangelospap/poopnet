package com.poopvibe.app.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * Stores the application profile mapped to an external authentication identity.
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String authProviderId;

    @Column(nullable = false, unique = true, length = 40)
    private String username;

    @Column(nullable = false, unique = true, length = 160)
    private String email;

    @Column(length = 500)
    private String profilePicUrl;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected User() {
    }

    /**
     * Creates a user profile with the identity data required by the application.
     *
     * @param authProviderId stable external authentication identifier
     * @param username public display handle
     * @param email unique email address
     * @param profilePicUrl optional profile image URL
     */
    public User(String authProviderId, String username, String email, String profilePicUrl) {
        this.authProviderId = authProviderId;
        this.username = username;
        this.email = email;
        this.profilePicUrl = profilePicUrl;
    }

    /**
     * Updates mutable profile fields.
     *
     * @param username public display handle
     * @param email unique email address
     * @param profilePicUrl optional profile image URL
     */
    public void updateProfile(String username, String email, String profilePicUrl) {
        this.username = username;
        this.email = email;
        this.profilePicUrl = profilePicUrl;
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

    public String getAuthProviderId() {
        return authProviderId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}

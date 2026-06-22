package com.poopvibe.app.session;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Persists media metadata attached to sessions.
 */
public interface SessionMediaRepository extends JpaRepository<SessionMedia, Long> {

    /**
     * Lists media attachments for a session.
     *
     * @param sessionId target session identifier
     * @return media metadata rows
     */
    List<SessionMedia> findBySessionId(Long sessionId);
}

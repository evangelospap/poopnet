package com.poopvibe.app.session;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Persists comments attached to sessions.
 */
public interface SessionCommentRepository extends JpaRepository<SessionComment, Long> {

    /**
     * Lists comments for a session newest last for conversational rendering.
     *
     * @param sessionId target session identifier
     * @return comments ordered oldest first
     */
    List<SessionComment> findBySessionIdOrderByCreatedAtAsc(Long sessionId);
}

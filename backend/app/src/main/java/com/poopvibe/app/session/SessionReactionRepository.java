package com.poopvibe.app.session;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Persists emoji reactions attached to sessions.
 */
public interface SessionReactionRepository extends JpaRepository<SessionReaction, Long> {

    /**
     * Finds a user's existing reaction for replacement semantics.
     *
     * @param sessionId target session identifier
     * @param userId reacting user identifier
     * @return reaction row, or empty when the user has not reacted
     */
    Optional<SessionReaction> findBySessionIdAndUserId(Long sessionId, Long userId);

    /**
     * Lists reactions for a session.
     *
     * @param sessionId target session identifier
     * @return reactions on the session
     */
    List<SessionReaction> findBySessionId(Long sessionId);
}

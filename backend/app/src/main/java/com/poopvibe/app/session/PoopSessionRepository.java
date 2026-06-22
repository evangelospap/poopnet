package com.poopvibe.app.session;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Persists session logs and supports feed/stat queries.
 */
public interface PoopSessionRepository extends JpaRepository<PoopSession, Long> {

    /**
     * Finds a session by its frontend-generated client ID for idempotent offline sync.
     *
     * @param clientId frontend-generated session identifier
     * @return matching session, or empty when it has not been synced
     */
    Optional<PoopSession> findByClientId(UUID clientId);

    /**
     * Lists sessions owned by a user newest first.
     *
     * @param userId owner user identifier
     * @return user's sessions ordered newest first
     */
    List<PoopSession> findByUserIdOrderByStartTimeDesc(Long userId);

    /**
     * Lists recent visible sessions for a set of users.
     *
     * @param userIds owner user identifiers
     * @param privacyLevels allowed privacy levels
     * @return recent sessions matching owners and visibility
     */
    List<PoopSession> findTop50ByUserIdInAndPrivacyInOrderByStartTimeDesc(
            Collection<Long> userIds,
            Collection<SessionPrivacy> privacyLevels
    );

    /**
     * Lists sessions for a user that overlap a reporting range.
     *
     * @param userId owner user identifier
     * @param start inclusive range start
     * @param end exclusive range end
     * @return matching sessions
     */
    List<PoopSession> findByUserIdAndStartTimeGreaterThanEqualAndStartTimeLessThan(Long userId, Instant start, Instant end);
}

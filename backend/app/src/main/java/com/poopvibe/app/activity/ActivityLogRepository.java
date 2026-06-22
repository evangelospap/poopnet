package com.poopvibe.app.activity;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Persists activity records for auditing and operational diagnostics.
 */
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    /**
     * Returns the latest activity records for an actor.
     *
     * @param actorUserId user identifier responsible for the activity
     * @return recent activity records ordered newest first
     */
    List<ActivityLog> findTop50ByActorUserIdOrderByCreatedAtDesc(Long actorUserId);
}

package com.poopvibe.app.activity;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Records durable activity events and increments operational activity metrics.
 */
@Service
public class ActivityLogService {
    private final ActivityLogRepository repository;
    private final Counter activityCounter;

    /**
     * Creates the logging service.
     *
     * @param repository activity log repository
     * @param meterRegistry registry used for activity counters
     */
    public ActivityLogService(ActivityLogRepository repository, MeterRegistry meterRegistry) {
        this.repository = repository;
        this.activityCounter = Counter.builder("poop_vibe_activity_events_total")
                .description("Total domain activity events recorded")
                .register(meterRegistry);
    }

    /**
     * Records an activity entry in its own transaction so audit writes survive caller rollbacks where possible.
     *
     * @param actorUserId user responsible for the activity, or null for system activity
     * @param type activity type
     * @param targetType target resource category
     * @param targetId target resource identifier
     * @param details short details for operators
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(Long actorUserId, ActivityType type, String targetType, Long targetId, String details) {
        repository.save(new ActivityLog(actorUserId, type, targetType, targetId, details));
        activityCounter.increment();
    }

    /**
     * Returns recent activity for an actor.
     *
     * @param actorUserId actor user identifier
     * @return recent activity records newest first
     */
    @Transactional(readOnly = true)
    public List<ActivityLogDtos.ActivityLogResponse> recentForActor(Long actorUserId) {
        return repository.findTop50ByActorUserIdOrderByCreatedAtDesc(actorUserId)
                .stream()
                .map(ActivityLogDtos.ActivityLogResponse::from)
                .toList();
    }
}

package com.poopvibe.app.activity;

import java.time.Instant;

/**
 * Response DTOs for activity log APIs.
 */
public final class ActivityLogDtos {
    private ActivityLogDtos() {
    }

    /**
     * Activity log entry returned to API callers.
     */
    public record ActivityLogResponse(
            Long id,
            Long actorUserId,
            ActivityType type,
            String targetType,
            Long targetId,
            String details,
            Instant createdAt
    ) {
        /**
         * Maps a persisted activity log into an API response.
         *
         * @param activityLog persisted activity log
         * @return API response for the supplied activity log
         */
        public static ActivityLogResponse from(ActivityLog activityLog) {
            return new ActivityLogResponse(
                    activityLog.getId(),
                    activityLog.getActorUserId(),
                    activityLog.getType(),
                    activityLog.getTargetType(),
                    activityLog.getTargetId(),
                    activityLog.getDetails(),
                    activityLog.getCreatedAt()
            );
        }
    }
}

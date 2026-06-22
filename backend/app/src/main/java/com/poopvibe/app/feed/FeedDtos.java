package com.poopvibe.app.feed;

import com.poopvibe.app.session.ComfortLevel;
import com.poopvibe.app.session.SessionMood;
import com.poopvibe.app.session.SessionPrivacy;
import java.time.Instant;

/**
 * Response DTOs for social feed APIs.
 */
public final class FeedDtos {
    private FeedDtos() {
    }

    /**
     * Feed item summarizing one visible session.
     */
    public record FeedItemResponse(
            Long sessionId,
            Long userId,
            String username,
            Instant startTime,
            long durationSeconds,
            SessionMood mood,
            ComfortLevel comfortLevel,
            SessionPrivacy privacy,
            long reactionCount,
            long commentCount,
            String summary
    ) {
    }
}

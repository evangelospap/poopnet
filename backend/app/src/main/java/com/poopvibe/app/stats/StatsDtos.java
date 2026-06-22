package com.poopvibe.app.stats;

import com.poopvibe.app.session.ComfortLevel;
import com.poopvibe.app.session.SessionMood;
import java.time.LocalDate;
import java.util.Map;

/**
 * Response DTOs for stats and trend APIs.
 */
public final class StatsDtos {
    private StatsDtos() {
    }

    /**
     * Per-day trend entry for charts.
     */
    public record DailyTrend(LocalDate day, long sessions, double averageDurationSeconds) {
    }

    /**
     * Aggregated stats summary for a user and date range.
     */
    public record StatsSummaryResponse(
            Long userId,
            long totalSessions,
            double averageDurationSeconds,
            long currentStreakDays,
            Map<SessionMood, Long> moodCounts,
            Map<ComfortLevel, Long> comfortCounts,
            java.util.List<DailyTrend> trends
    ) {
    }
}

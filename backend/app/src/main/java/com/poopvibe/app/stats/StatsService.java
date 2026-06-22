package com.poopvibe.app.stats;

import com.poopvibe.app.session.ComfortLevel;
import com.poopvibe.app.session.PoopSession;
import com.poopvibe.app.session.PoopSessionRepository;
import com.poopvibe.app.session.SessionMood;
import com.poopvibe.app.stats.StatsDtos.DailyTrend;
import com.poopvibe.app.stats.StatsDtos.StatsSummaryResponse;
import com.poopvibe.app.user.UserService;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Produces lightweight session aggregates for dashboards and charts.
 */
@Service
public class StatsService {
    private final PoopSessionRepository sessionRepository;
    private final UserService userService;

    /**
     * Creates the stats service.
     *
     * @param sessionRepository session repository
     * @param userService user lookup service
     */
    public StatsService(PoopSessionRepository sessionRepository, UserService userService) {
        this.sessionRepository = sessionRepository;
        this.userService = userService;
    }

    /**
     * Calculates summary stats for a user over a date range.
     *
     * @param userId owner user identifier
     * @param from inclusive date range start, defaulting to 30 days ago
     * @param to inclusive date range end, defaulting to today
     * @return aggregate stats for the requested user and range
     */
    @Transactional(readOnly = true)
    public StatsSummaryResponse summary(Long userId, LocalDate from, LocalDate to) {
        userService.findEntity(userId);
        LocalDate endDay = to == null ? LocalDate.now(ZoneOffset.UTC) : to;
        LocalDate startDay = from == null ? endDay.minusDays(29) : from;
        Instant start = startDay.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant end = endDay.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);
        List<PoopSession> sessions = sessionRepository.findByUserIdAndStartTimeGreaterThanEqualAndStartTimeLessThan(userId, start, end);

        double averageDuration = sessions.stream().mapToLong(PoopSession::getDurationSeconds).average().orElse(0);
        Map<SessionMood, Long> moodCounts = countByMood(sessions);
        Map<ComfortLevel, Long> comfortCounts = countByComfort(sessions);
        List<DailyTrend> trends = buildTrends(sessions);

        return new StatsSummaryResponse(
                userId,
                sessions.size(),
                averageDuration,
                calculateStreak(userId),
                moodCounts,
                comfortCounts,
                trends
        );
    }

    private Map<SessionMood, Long> countByMood(List<PoopSession> sessions) {
        Map<SessionMood, Long> counts = new EnumMap<>(SessionMood.class);
        sessions.forEach(session -> counts.merge(session.getMood(), 1L, Long::sum));
        return counts;
    }

    private Map<ComfortLevel, Long> countByComfort(List<PoopSession> sessions) {
        Map<ComfortLevel, Long> counts = new EnumMap<>(ComfortLevel.class);
        sessions.forEach(session -> counts.merge(session.getComfortLevel(), 1L, Long::sum));
        return counts;
    }

    private List<DailyTrend> buildTrends(List<PoopSession> sessions) {
        return sessions.stream()
                .collect(Collectors.groupingBy(session -> LocalDate.ofInstant(session.getStartTime(), ZoneOffset.UTC)))
                .entrySet()
                .stream()
                .map(entry -> new DailyTrend(
                        entry.getKey(),
                        entry.getValue().size(),
                        entry.getValue().stream().mapToLong(PoopSession::getDurationSeconds).average().orElse(0)
                ))
                .sorted(Comparator.comparing(DailyTrend::day))
                .toList();
    }

    private long calculateStreak(Long userId) {
        List<LocalDate> sessionDays = sessionRepository.findByUserIdOrderByStartTimeDesc(userId).stream()
                .map(session -> LocalDate.ofInstant(session.getStartTime(), ZoneOffset.UTC))
                .distinct()
                .toList();
        LocalDate cursor = LocalDate.now(ZoneOffset.UTC);
        long streak = 0;
        for (LocalDate day : sessionDays) {
            if (day.equals(cursor)) {
                streak++;
                cursor = cursor.minusDays(1);
            } else if (day.isBefore(cursor)) {
                break;
            }
        }
        return streak;
    }
}

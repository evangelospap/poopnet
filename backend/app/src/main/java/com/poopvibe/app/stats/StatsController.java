package com.poopvibe.app.stats;

import com.poopvibe.app.stats.StatsDtos.StatsSummaryResponse;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API for dashboard stats and trend summaries.
 */
@RestController
@RequestMapping("/api/v1/stats")
@RequiredArgsConstructor
public class StatsController {
    private final StatsService service;

    /**
     * Returns aggregate stats for a user and optional date range.
     *
     * @param userId owner user identifier
     * @param from inclusive date range start
     * @param to inclusive date range end
     * @return aggregate stats summary
     */
    @GetMapping("/summary")
    public StatsSummaryResponse summary(
            @RequestParam Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return service.summary(userId, from, to);
    }
}

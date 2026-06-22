package com.poopvibe.app.activity;

import com.poopvibe.app.activity.ActivityLogDtos.ActivityLogResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API for reading recent activity log records.
 */
@RestController
@RequestMapping("/api/v1/activity-logs")
public class ActivityLogController {
    private final ActivityLogService service;

    /**
     * Creates the activity log controller.
     *
     * @param service activity log service
     */
    public ActivityLogController(ActivityLogService service) {
        this.service = service;
    }

    /**
     * Returns recent activity created by an actor.
     *
     * @param actorUserId actor user identifier
     * @return recent activity entries
     */
    @GetMapping
    public List<ActivityLogResponse> recentForActor(@RequestParam Long actorUserId) {
        return service.recentForActor(actorUserId);
    }
}

package com.poopvibe.app.feed;

import com.poopvibe.app.feed.FeedDtos.FeedItemResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API for privacy-aware friend activity feed entries.
 */
@RestController
@RequestMapping("/api/v1/feed")
public class FeedController {
    private final FeedService service;

    /**
     * Creates the feed controller.
     *
     * @param service feed service
     */
    public FeedController(FeedService service) {
        this.service = service;
    }

    /**
     * Returns the viewer's activity feed.
     *
     * @param userId viewer user identifier
     * @return visible feed items
     */
    @GetMapping
    public List<FeedItemResponse> feed(@RequestParam Long userId) {
        return service.feedForUser(userId);
    }
}

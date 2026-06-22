package com.poopvibe.app.feed;

import com.poopvibe.app.feed.FeedDtos.FeedItemResponse;
import com.poopvibe.app.friendship.Friendship;
import com.poopvibe.app.friendship.FriendshipService;
import com.poopvibe.app.session.PoopSession;
import com.poopvibe.app.session.PoopSessionRepository;
import com.poopvibe.app.session.SessionCommentRepository;
import com.poopvibe.app.session.SessionPrivacy;
import com.poopvibe.app.session.SessionReactionRepository;
import com.poopvibe.app.user.UserService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Builds privacy-aware activity feed read models.
 */
@Service
public class FeedService {
    private final PoopSessionRepository sessionRepository;
    private final SessionReactionRepository reactionRepository;
    private final SessionCommentRepository commentRepository;
    private final FriendshipService friendshipService;
    private final UserService userService;

    /**
     * Creates the feed service.
     *
     * @param sessionRepository session repository
     * @param reactionRepository reaction repository
     * @param commentRepository comment repository
     * @param friendshipService friendship lookup service
     * @param userService user lookup service
     */
    public FeedService(
            PoopSessionRepository sessionRepository,
            SessionReactionRepository reactionRepository,
            SessionCommentRepository commentRepository,
            FriendshipService friendshipService,
            UserService userService
    ) {
        this.sessionRepository = sessionRepository;
        this.reactionRepository = reactionRepository;
        this.commentRepository = commentRepository;
        this.friendshipService = friendshipService;
        this.userService = userService;
    }

    /**
     * Returns the current user's feed using accepted friendships and session privacy.
     *
     * @param userId viewer user identifier
     * @return visible feed items newest first
     */
    @Transactional(readOnly = true)
    public List<FeedItemResponse> feedForUser(Long userId) {
        userService.findEntity(userId);
        List<Long> friendIds = friendshipService.acceptedForUser(userId).stream()
                .map(friendship -> otherUserId(friendship, userId))
                .toList();

        List<PoopSession> visible = new ArrayList<>(sessionRepository.findByUserIdOrderByStartTimeDesc(userId));
        if (!friendIds.isEmpty()) {
            visible.addAll(sessionRepository.findTop50ByUserIdInAndPrivacyInOrderByStartTimeDesc(
                    friendIds,
                    List.of(SessionPrivacy.FRIENDS, SessionPrivacy.PUBLIC)
            ));
        }

        return visible.stream()
                .sorted(Comparator.comparing(PoopSession::getStartTime).reversed())
                .limit(50)
                .map(this::toFeedItem)
                .toList();
    }

    private Long otherUserId(Friendship friendship, Long userId) {
        Long requesterId = friendship.getRequester().getId();
        return requesterId.equals(userId) ? friendship.getAddressee().getId() : requesterId;
    }

    private FeedItemResponse toFeedItem(PoopSession session) {
        return new FeedItemResponse(
                session.getId(),
                session.getUser().getId(),
                session.getUser().getUsername(),
                session.getStartTime(),
                session.getDurationSeconds(),
                session.getMood(),
                session.getComfortLevel(),
                session.getPrivacy(),
                reactionRepository.findBySessionId(session.getId()).size(),
                commentRepository.findBySessionIdOrderByCreatedAtAsc(session.getId()).size(),
                "%s logged a %s vibe".formatted(session.getUser().getUsername(), session.getMood().name().toLowerCase())
        );
    }
}

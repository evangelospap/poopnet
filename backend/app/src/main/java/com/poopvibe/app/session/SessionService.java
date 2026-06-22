package com.poopvibe.app.session;

import com.poopvibe.app.activity.ActivityLogService;
import com.poopvibe.app.activity.ActivityType;
import com.poopvibe.app.common.BusinessRuleException;
import com.poopvibe.app.common.ResourceNotFoundException;
import com.poopvibe.app.session.SessionDtos.AddCommentRequest;
import com.poopvibe.app.session.SessionDtos.AddMediaRequest;
import com.poopvibe.app.session.SessionDtos.AddReactionRequest;
import com.poopvibe.app.session.SessionDtos.CommentResponse;
import com.poopvibe.app.session.SessionDtos.MediaResponse;
import com.poopvibe.app.session.SessionDtos.ReactionResponse;
import com.poopvibe.app.session.SessionDtos.SessionResponse;
import com.poopvibe.app.session.SessionDtos.UpsertSessionRequest;
import com.poopvibe.app.user.User;
import com.poopvibe.app.user.UserService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Coordinates session logging, child resources, and idempotent offline sync behavior.
 */
@Service
public class SessionService {
    private final PoopSessionRepository sessionRepository;
    private final SessionReactionRepository reactionRepository;
    private final SessionCommentRepository commentRepository;
    private final SessionMediaRepository mediaRepository;
    private final UserService userService;
    private final ActivityLogService activityLogService;
    private final Counter sessionCreatedCounter;

    /**
     * Creates the session service.
     *
     * @param sessionRepository session repository
     * @param reactionRepository reaction repository
     * @param commentRepository comment repository
     * @param mediaRepository media repository
     * @param userService user lookup service
     * @param activityLogService activity logger
     * @param meterRegistry registry used for session counters
     */
    public SessionService(
            PoopSessionRepository sessionRepository,
            SessionReactionRepository reactionRepository,
            SessionCommentRepository commentRepository,
            SessionMediaRepository mediaRepository,
            UserService userService,
            ActivityLogService activityLogService,
            MeterRegistry meterRegistry
    ) {
        this.sessionRepository = sessionRepository;
        this.reactionRepository = reactionRepository;
        this.commentRepository = commentRepository;
        this.mediaRepository = mediaRepository;
        this.userService = userService;
        this.activityLogService = activityLogService;
        this.sessionCreatedCounter = Counter.builder("poop_vibe_sessions_created_total")
                .description("Total sessions created or synced through the API")
                .register(meterRegistry);
    }

    /**
     * Creates a session or updates an existing session with the same client ID.
     *
     * @param request session payload
     * @return created or updated session
     */
    @Transactional
    public SessionResponse create(UpsertSessionRequest request) {
        validateTimes(request.startTime(), request.endTime());
        User user = userService.findEntity(request.userId());
        UUID clientId = request.clientId() == null ? UUID.randomUUID() : request.clientId();
        PoopSession session = sessionRepository.findByClientId(clientId)
                .map(existing -> {
                    existing.applyUpdate(
                            request.startTime(),
                            request.endTime(),
                            request.mood(),
                            request.comfortLevel(),
                            request.privacy(),
                            request.note(),
                            request.syncedFromOffline()
                    );
                    return existing;
                })
                .orElseGet(() -> new PoopSession(
                        clientId,
                        user,
                        request.startTime(),
                        request.endTime(),
                        request.mood(),
                        request.comfortLevel(),
                        request.privacy(),
                        request.note(),
                        request.syncedFromOffline()
                ));

        PoopSession saved = sessionRepository.save(session);
        sessionCreatedCounter.increment();
        activityLogService.record(user.getId(), ActivityType.SESSION_CREATED, "session", saved.getId(), saved.getMood().name());
        return toResponse(saved);
    }

    /**
     * Updates an existing session.
     *
     * @param sessionId session identifier
     * @param request session payload
     * @return updated session
     */
    @Transactional
    public SessionResponse update(Long sessionId, UpsertSessionRequest request) {
        validateTimes(request.startTime(), request.endTime());
        PoopSession session = findEntity(sessionId);
        userService.findEntity(request.userId());
        if (!session.getUser().getId().equals(request.userId())) {
            throw new BusinessRuleException("Session owner cannot be changed.");
        }
        session.applyUpdate(
                request.startTime(),
                request.endTime(),
                request.mood(),
                request.comfortLevel(),
                request.privacy(),
                request.note(),
                request.syncedFromOffline()
        );
        activityLogService.record(request.userId(), ActivityType.SESSION_UPDATED, "session", session.getId(), session.getMood().name());
        return toResponse(session);
    }

    /**
     * Deletes a session.
     *
     * @param sessionId session identifier
     */
    @Transactional
    public void delete(Long sessionId) {
        PoopSession session = findEntity(sessionId);
        Long userId = session.getUser().getId();
        sessionRepository.delete(session);
        activityLogService.record(userId, ActivityType.SESSION_DELETED, "session", sessionId, "deleted");
    }

    /**
     * Returns a session with child resources.
     *
     * @param sessionId session identifier
     * @return matching session response
     */
    @Transactional(readOnly = true)
    public SessionResponse get(Long sessionId) {
        return toResponse(findEntity(sessionId));
    }

    /**
     * Lists sessions for a user.
     *
     * @param userId owner user identifier
     * @return user's sessions newest first
     */
    @Transactional(readOnly = true)
    public List<SessionResponse> listForUser(Long userId) {
        userService.findEntity(userId);
        return sessionRepository.findByUserIdOrderByStartTimeDesc(userId).stream().map(this::toResponse).toList();
    }

    /**
     * Adds or replaces a user's reaction on a session.
     *
     * @param sessionId session identifier
     * @param request reaction payload
     * @return saved reaction
     */
    @Transactional
    public ReactionResponse addReaction(Long sessionId, AddReactionRequest request) {
        PoopSession session = findEntity(sessionId);
        User user = userService.findEntity(request.userId());
        SessionReaction reaction = reactionRepository.findBySessionIdAndUserId(sessionId, request.userId())
                .map(existing -> {
                    existing.updateEmoji(request.emoji());
                    return existing;
                })
                .orElseGet(() -> new SessionReaction(session, user, request.emoji()));
        SessionReaction saved = reactionRepository.save(reaction);
        activityLogService.record(user.getId(), ActivityType.REACTION_ADDED, "session", sessionId, request.emoji());
        return ReactionResponse.from(saved);
    }

    /**
     * Adds a comment to a session.
     *
     * @param sessionId session identifier
     * @param request comment payload
     * @return saved comment
     */
    @Transactional
    public CommentResponse addComment(Long sessionId, AddCommentRequest request) {
        PoopSession session = findEntity(sessionId);
        User user = userService.findEntity(request.userId());
        SessionComment saved = commentRepository.save(new SessionComment(session, user, request.body()));
        activityLogService.record(user.getId(), ActivityType.COMMENT_ADDED, "session", sessionId, request.body());
        return CommentResponse.from(saved);
    }

    /**
     * Adds media metadata to a session.
     *
     * @param sessionId session identifier
     * @param request media payload
     * @return saved media metadata
     */
    @Transactional
    public MediaResponse addMedia(Long sessionId, AddMediaRequest request) {
        PoopSession session = findEntity(sessionId);
        SessionMedia saved = mediaRepository.save(new SessionMedia(session, request.mediaUrl(), request.mediaType()));
        return MediaResponse.from(saved);
    }

    /**
     * Resolves a session entity or raises a not-found exception.
     *
     * @param sessionId session identifier
     * @return matching session entity
     */
    @Transactional(readOnly = true)
    public PoopSession findEntity(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session %d was not found.".formatted(sessionId)));
    }

    /**
     * Maps a session entity to its full response shape.
     *
     * @param session persisted session entity
     * @return full session response
     */
    public SessionResponse toResponse(PoopSession session) {
        return SessionResponse.from(
                session,
                reactionRepository.findBySessionId(session.getId()),
                commentRepository.findBySessionIdOrderByCreatedAtAsc(session.getId()),
                mediaRepository.findBySessionId(session.getId())
        );
    }

    private void validateTimes(Instant startTime, Instant endTime) {
        if (!endTime.isAfter(startTime)) {
            throw new BusinessRuleException("Session endTime must be after startTime.");
        }
    }
}

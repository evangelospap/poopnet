package com.poopvibe.app.session;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Request and response DTOs for session, reaction, comment, and media APIs.
 */
public final class SessionDtos {
    private SessionDtos() {
    }

    /**
     * Request body used to create or update a session log.
     */
    public record UpsertSessionRequest(
            UUID clientId,
            @NotNull Long userId,
            @NotNull Instant startTime,
            @NotNull Instant endTime,
            @NotNull SessionMood mood,
            @NotNull ComfortLevel comfortLevel,
            @NotNull SessionPrivacy privacy,
            @Size(max = 1000) String note,
            boolean syncedFromOffline
    ) {
    }

    /**
     * Request body used to add or replace a reaction.
     */
    public record AddReactionRequest(
            @NotNull Long userId,
            @NotBlank @Size(max = 12) String emoji
    ) {
    }

    /**
     * Request body used to add a comment.
     */
    public record AddCommentRequest(
            @NotNull Long userId,
            @NotBlank @Size(max = 500) String body
    ) {
    }

    /**
     * Request body used to attach media metadata.
     */
    public record AddMediaRequest(
            @NotBlank @Size(max = 500) String mediaUrl,
            @NotNull MediaType mediaType
    ) {
    }

    /**
     * Reaction response returned with session details.
     */
    public record ReactionResponse(
            Long id,
            Long userId,
            String username,
            String emoji,
            Instant createdAt
    ) {
        /**
         * Maps a persisted reaction into a transport response.
         *
         * @param reaction persisted reaction entity
         * @return API response for the supplied reaction
         */
        public static ReactionResponse from(SessionReaction reaction) {
            return new ReactionResponse(
                    reaction.getId(),
                    reaction.getUser().getId(),
                    reaction.getUser().getUsername(),
                    reaction.getEmoji(),
                    reaction.getCreatedAt()
            );
        }
    }

    /**
     * Comment response returned with session details.
     */
    public record CommentResponse(
            Long id,
            Long userId,
            String username,
            String body,
            Instant createdAt,
            Instant updatedAt
    ) {
        /**
         * Maps a persisted comment into a transport response.
         *
         * @param comment persisted comment entity
         * @return API response for the supplied comment
         */
        public static CommentResponse from(SessionComment comment) {
            return new CommentResponse(
                    comment.getId(),
                    comment.getUser().getId(),
                    comment.getUser().getUsername(),
                    comment.getBody(),
                    comment.getCreatedAt(),
                    comment.getUpdatedAt()
            );
        }
    }

    /**
     * Media response returned with session details.
     */
    public record MediaResponse(
            Long id,
            String mediaUrl,
            MediaType mediaType,
            Instant createdAt
    ) {
        /**
         * Maps persisted media metadata into a transport response.
         *
         * @param media persisted media entity
         * @return API response for the supplied media row
         */
        public static MediaResponse from(SessionMedia media) {
            return new MediaResponse(media.getId(), media.getMediaUrl(), media.getMediaType(), media.getCreatedAt());
        }
    }

    /**
     * Session response returned to API callers.
     */
    public record SessionResponse(
            Long id,
            UUID clientId,
            Long userId,
            String username,
            Instant startTime,
            Instant endTime,
            long durationSeconds,
            SessionMood mood,
            ComfortLevel comfortLevel,
            SessionPrivacy privacy,
            String note,
            boolean syncedFromOffline,
            Instant createdAt,
            Instant updatedAt,
            List<ReactionResponse> reactions,
            List<CommentResponse> comments,
            List<MediaResponse> media
    ) {
        /**
         * Maps a session and its child resources into a transport response.
         *
         * @param session persisted session entity
         * @param reactions reactions attached to the session
         * @param comments comments attached to the session
         * @param media media attached to the session
         * @return API response for the supplied session
         */
        public static SessionResponse from(
                PoopSession session,
                List<SessionReaction> reactions,
                List<SessionComment> comments,
                List<SessionMedia> media
        ) {
            return new SessionResponse(
                    session.getId(),
                    session.getClientId(),
                    session.getUser().getId(),
                    session.getUser().getUsername(),
                    session.getStartTime(),
                    session.getEndTime(),
                    session.getDurationSeconds(),
                    session.getMood(),
                    session.getComfortLevel(),
                    session.getPrivacy(),
                    session.getNote(),
                    session.isSyncedFromOffline(),
                    session.getCreatedAt(),
                    session.getUpdatedAt(),
                    reactions.stream().map(ReactionResponse::from).toList(),
                    comments.stream().map(CommentResponse::from).toList(),
                    media.stream().map(MediaResponse::from).toList()
            );
        }
    }
}

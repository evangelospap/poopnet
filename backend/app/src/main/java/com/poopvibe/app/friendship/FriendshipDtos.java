package com.poopvibe.app.friendship;

import com.poopvibe.app.user.User;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

/**
 * Request and response DTOs for friendship APIs.
 */
public final class FriendshipDtos {
    private FriendshipDtos() {
    }

    /**
     * Request body used to create a friend request.
     */
    public record CreateFriendRequest(
            @NotNull Long requesterId,
            @NotNull Long addresseeId
    ) {
    }

    /**
     * Request body used to update a friend request or relationship.
     */
    public record UpdateFriendshipRequest(@NotNull FriendshipStatus status) {
    }

    /**
     * Friendship returned to API callers.
     */
    public record FriendshipResponse(
            Long id,
            Long requesterId,
            String requesterUsername,
            Long addresseeId,
            String addresseeUsername,
            FriendshipStatus status,
            Instant createdAt,
            Instant updatedAt
    ) {
        /**
         * Maps a friendship entity into an API response.
         *
         * @param friendship persisted friendship entity
         * @return API response for the supplied friendship
         */
        public static FriendshipResponse from(Friendship friendship) {
            User requester = friendship.getRequester();
            User addressee = friendship.getAddressee();
            return new FriendshipResponse(
                    friendship.getId(),
                    requester.getId(),
                    requester.getUsername(),
                    addressee.getId(),
                    addressee.getUsername(),
                    friendship.getStatus(),
                    friendship.getCreatedAt(),
                    friendship.getUpdatedAt()
            );
        }
    }
}

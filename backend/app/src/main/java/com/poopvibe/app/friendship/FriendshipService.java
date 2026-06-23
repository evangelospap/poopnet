package com.poopvibe.app.friendship;

import com.poopvibe.app.activity.ActivityLogService;
import com.poopvibe.app.activity.ActivityType;
import com.poopvibe.app.common.BusinessRuleException;
import com.poopvibe.app.common.ResourceNotFoundException;
import com.poopvibe.app.friendship.FriendshipDtos.CreateFriendRequest;
import com.poopvibe.app.friendship.FriendshipDtos.FriendshipResponse;
import com.poopvibe.app.friendship.FriendshipDtos.UpdateFriendshipRequest;
import com.poopvibe.app.user.User;
import com.poopvibe.app.user.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Manages friend requests and relationship status transitions.
 */
@Service
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendshipRepository repository;
    private final UserService userService;
    private final ActivityLogService activityLogService;

    /**
     * Creates a pending friend request.
     *
     * @param request friend request payload
     * @return created friendship
     */
    @Transactional
    public FriendshipResponse request(CreateFriendRequest request) {
        if (request.requesterId().equals(request.addresseeId())) {
            throw new BusinessRuleException("Users cannot send friend requests to themselves.");
        }
        repository.findBetween(request.requesterId(), request.addresseeId())
                .ifPresent(existing -> {
                    throw new BusinessRuleException("A friendship or request already exists for these users.");
                });

        User requester = userService.findEntity(request.requesterId());
        User addressee = userService.findEntity(request.addresseeId());
        Friendship friendship = repository.save(new Friendship(requester, addressee));
        activityLogService.recordActivity(requester.getId(), ActivityType.FRIEND_REQUESTED, "friendship", friendship.getId(), addressee.getUsername());
        return FriendshipResponse.from(friendship);
    }

    /**
     * Updates the status of a friend request or relationship.
     *
     * @param friendshipId friendship identifier
     * @param request status update request
     * @return updated friendship
     */
    @Transactional
    public FriendshipResponse update(Long friendshipId, UpdateFriendshipRequest request) {
        Friendship friendship = findEntity(friendshipId);
        friendship.updateStatus(request.status());
        activityLogService.recordActivity(
                friendship.getRequester().getId(),
                ActivityType.FRIENDSHIP_UPDATED,
                "friendship",
                friendship.getId(),
                request.status().name()
        );
        return FriendshipResponse.from(friendship);
    }

    /**
     * Lists all relationships touching a user.
     *
     * @param userId user identifier
     * @return friend requests and relationships involving the user
     */
    @Transactional(readOnly = true)
    public List<FriendshipResponse> listForUser(Long userId) {
        userService.findEntity(userId);
        return repository.findForUser(userId).stream().map(FriendshipResponse::from).toList();
    }

    /**
     * Lists accepted relationships touching a user.
     *
     * @param userId user identifier
     * @return accepted friendships involving the user
     */
    @Transactional(readOnly = true)
    public List<Friendship> acceptedForUser(Long userId) {
        return repository.findByUserAndStatus(userId, FriendshipStatus.ACCEPTED);
    }

    /**
     * Resolves a friendship entity or raises a not-found exception.
     *
     * @param friendshipId friendship identifier
     * @return matching friendship entity
     */
    @Transactional(readOnly = true)
    public Friendship findEntity(Long friendshipId) {
        return repository.findById(friendshipId)
                .orElseThrow(() -> new ResourceNotFoundException("Friendship %d was not found.".formatted(friendshipId)));
    }
}

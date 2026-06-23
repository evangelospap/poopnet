package com.poopvibe.app.friendship;

import com.poopvibe.app.friendship.FriendshipDtos.CreateFriendRequest;
import com.poopvibe.app.friendship.FriendshipDtos.FriendshipResponse;
import com.poopvibe.app.friendship.FriendshipDtos.UpdateFriendshipRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API for friend requests and relationship state changes.
 */
@RestController
@RequestMapping("/api/v1/friendships")
@RequiredArgsConstructor
public class FriendshipController {
    private final FriendshipService service;

    /**
     * Creates a pending friend request.
     *
     * @param request friend request payload
     * @return created friendship
     */
    @PostMapping
    public ResponseEntity<FriendshipResponse> request(@Valid @RequestBody CreateFriendRequest request) {
        FriendshipResponse response = service.request(request);
        return ResponseEntity.created(URI.create("/api/v1/friendships/" + response.id())).body(response);
    }

    /**
     * Lists friend requests and relationships for a user.
     *
     * @param userId user identifier
     * @return user's relationships
     */
    @GetMapping
    public List<FriendshipResponse> listForUser(@RequestParam Long userId) {
        return service.listForUser(userId);
    }

    /**
     * Updates relationship status.
     *
     * @param friendshipId friendship identifier
     * @param request status update request
     * @return updated friendship
     */
    @PatchMapping("/{friendshipId}")
    public FriendshipResponse update(
            @PathVariable Long friendshipId,
            @Valid @RequestBody UpdateFriendshipRequest request
    ) {
        return service.update(friendshipId, request);
    }
}

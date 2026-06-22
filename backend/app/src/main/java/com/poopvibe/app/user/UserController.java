package com.poopvibe.app.user;

import com.poopvibe.app.user.UserDtos.CreateUserRequest;
import com.poopvibe.app.user.UserDtos.UpdateUserRequest;
import com.poopvibe.app.user.UserDtos.UserResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
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
 * REST API for user profile creation, lookup, search, and updates.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService service;

    /**
     * Creates the user controller.
     *
     * @param service user service
     */
    public UserController(UserService service) {
        this.service = service;
    }

    /**
     * Creates a user profile.
     *
     * @param request creation request
     * @return created user response
     */
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        UserResponse response = service.create(request);
        return ResponseEntity.created(URI.create("/api/v1/users/" + response.id())).body(response);
    }

    /**
     * Lists or searches user profiles.
     *
     * @param query optional username search fragment
     * @return matching user profiles
     */
    @GetMapping
    public List<UserResponse> list(@RequestParam(required = false) String query) {
        return service.search(query);
    }

    /**
     * Returns a user profile.
     *
     * @param userId user identifier
     * @return matching user profile
     */
    @GetMapping("/{userId}")
    public UserResponse get(@PathVariable Long userId) {
        return service.get(userId);
    }

    /**
     * Updates a user profile.
     *
     * @param userId user identifier
     * @param request update request
     * @return updated user profile
     */
    @PatchMapping("/{userId}")
    public UserResponse update(@PathVariable Long userId, @Valid @RequestBody UpdateUserRequest request) {
        return service.update(userId, request);
    }
}

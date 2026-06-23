package com.poopvibe.app.user;

import com.poopvibe.app.activity.ActivityLogService;
import com.poopvibe.app.activity.ActivityType;
import com.poopvibe.app.common.ResourceNotFoundException;
import com.poopvibe.app.user.UserDtos.CreateUserRequest;
import com.poopvibe.app.user.UserDtos.UpdateUserRequest;
import com.poopvibe.app.user.UserDtos.UserResponse;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Coordinates user profile creation, lookup, search, and updates.
 */
@Service
public class UserService {
    private final UserRepository repository;
    private final ActivityLogService activityLogService;
    private final Counter userCreatedCounter;

    /**
     * Creates the user service.
     *
     * @param repository user repository
     * @param activityLogService activity logger
     * @param meterRegistry registry used for user counters
     */
    public UserService(UserRepository repository, ActivityLogService activityLogService, MeterRegistry meterRegistry) {
        this.repository = repository;
        this.activityLogService = activityLogService;
        this.userCreatedCounter = Counter.builder("poop_vibe_users_created_total")
                .description("Total users created through the API")
                .register(meterRegistry);
    }

    /**
     * Creates a user profile.
     *
     * @param request creation request
     * @return created user response
     */
    @Transactional
    public UserResponse create(CreateUserRequest request) {
        User user = repository.save(new User(
                request.authProviderId(),
                request.username(),
                request.email(),
                request.profilePicUrl()
        ));
        userCreatedCounter.increment();
        activityLogService.recordActivity(user.getId(), ActivityType.USER_CREATED, "user", user.getId(), user.getUsername());
        return UserResponse.from(user);
    }

    /**
     * Updates a user profile.
     *
     * @param userId user identifier
     * @param request update request
     * @return updated user response
     */
    @Transactional
    public UserResponse update(Long userId, UpdateUserRequest request) {
        User user = findEntity(userId);
        user.updateProfile(request.username(), request.email(), request.profilePicUrl());
        activityLogService.recordActivity(user.getId(), ActivityType.USER_UPDATED, "user", user.getId(), user.getUsername());
        return UserResponse.from(user);
    }

    /**
     * Returns a user by identifier.
     *
     * @param userId user identifier
     * @return matching user response
     */
    @Transactional(readOnly = true)
    public UserResponse get(Long userId) {
        return UserResponse.from(findEntity(userId));
    }

    /**
     * Lists all user profiles.
     *
     * @return all users in repository order
     */
    @Transactional(readOnly = true)
    public List<UserResponse> list() {
        return repository.findAll().stream().map(UserResponse::from).toList();
    }

    /**
     * Searches users by username.
     *
     * @param query username fragment; blank values return all users
     * @return matching users
     */
    @Transactional(readOnly = true)
    public List<UserResponse> search(String query) {
        if (query == null || query.isBlank()) {
            return list();
        }
        return repository.findTop20ByUsernameContainingIgnoreCase(query).stream().map(UserResponse::from).toList();
    }

    /**
     * Resolves a user entity or raises a not-found exception.
     *
     * @param userId user identifier
     * @return matching user entity
     */
    @Transactional(readOnly = true)
    public User findEntity(Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User %d was not found.".formatted(userId)));
    }
}

package com.poopvibe.app.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;

/**
 * Request and response DTOs for user profile APIs.
 */
public final class UserDtos {
    private UserDtos() {
    }

    /**
     * Request body used to create a user profile.
     */
    public record CreateUserRequest(
            @NotBlank @Size(max = 120) String authProviderId,
            @NotBlank @Size(max = 40) String username,
            @NotBlank @Email @Size(max = 160) String email,
            @Size(max = 500) String profilePicUrl
    ) {
    }

    /**
     * Request body used to update mutable profile details.
     */
    public record UpdateUserRequest(
            @NotBlank @Size(max = 40) String username,
            @NotBlank @Email @Size(max = 160) String email,
            @Size(max = 500) String profilePicUrl
    ) {
    }

    /**
     * User profile returned to API callers.
     */
    public record UserResponse(
            Long id,
            String authProviderId,
            String username,
            String email,
            String profilePicUrl,
            Instant createdAt,
            Instant updatedAt
    ) {
        /**
         * Maps a persisted user into a transport response.
         *
         * @param user persisted user entity
         * @return API response for the supplied user
         */
        public static UserResponse from(User user) {
            return new UserResponse(
                    user.getId(),
                    user.getAuthProviderId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getProfilePicUrl(),
                    user.getCreatedAt(),
                    user.getUpdatedAt()
            );
        }
    }
}

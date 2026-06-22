package com.poopvibe.app.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Persists user profiles and supports identity/display lookup.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a profile by the external authentication provider identifier.
     *
     * @param authProviderId stable authentication provider identifier
     * @return matching user, or empty when the identity has not been registered
     */
    Optional<User> findByAuthProviderId(String authProviderId);

    /**
     * Searches users by a case-insensitive username fragment.
     *
     * @param username username fragment supplied by the caller
     * @return matching users ordered by repository default order
     */
    List<User> findTop20ByUsernameContainingIgnoreCase(String username);
}

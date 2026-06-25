package com.poopvibe.app.device;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Persists browser and PWA device registrations.
 */
public interface DeviceRepository extends JpaRepository<Device, Long> {

    /**
     * Finds a device by browser push endpoint for idempotent registration.
     *
     * @param endpoint browser push endpoint supplied by the client
     * @return matching device, or empty when the endpoint is new
     */
    Optional<Device> findByEndpoint(String endpoint);

    /**
     * Lists devices owned by a user.
     *
     * @param userId owning user identifier
     * @return user's registered devices
     */
    List<Device> findByUserId(Long userId);

    /**
     * Lists enabled push subscriptions for a set of users.
     *
     * @param userIds recipient user identifiers
     * @return enabled devices owned by the requested users
     */
    List<Device> findByUserIdInAndPushEnabledTrue(Collection<Long> userIds);
}

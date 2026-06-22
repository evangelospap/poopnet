package com.poopvibe.app.device;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Persists browser and PWA device registrations.
 */
public interface DeviceRepository extends JpaRepository<Device, Long> {

    /**
     * Finds a device by push token for idempotent registration.
     *
     * @param fcmToken FCM token supplied by the client
     * @return matching device, or empty when the token is new
     */
    Optional<Device> findByFcmToken(String fcmToken);

    /**
     * Lists devices owned by a user.
     *
     * @param userId owning user identifier
     * @return user's registered devices
     */
    List<Device> findByUserId(Long userId);
}

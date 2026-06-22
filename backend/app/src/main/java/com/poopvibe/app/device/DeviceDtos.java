package com.poopvibe.app.device;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

/**
 * Request and response DTOs for device registration APIs.
 */
public final class DeviceDtos {
    private DeviceDtos() {
    }

    /**
     * Request body used to register or refresh a device token.
     */
    public record RegisterDeviceRequest(
            @NotNull Long userId,
            @NotBlank @Size(max = 512) String fcmToken,
            @NotNull Deviceapp app,
            boolean pushEnabled
    ) {
    }

    /**
     * Device registration returned to API callers.
     */
    public record DeviceResponse(
            Long id,
            Long userId,
            String fcmToken,
            Deviceapp app,
            boolean pushEnabled,
            Instant lastSeenAt,
            Instant createdAt,
            Instant updatedAt
    ) {
        /**
         * Maps a device entity into an API response.
         *
         * @param device persisted device entity
         * @return API response for the supplied device
         */
        public static DeviceResponse from(Device device) {
            return new DeviceResponse(
                    device.getId(),
                    device.getUser().getId(),
                    device.getFcmToken(),
                    device.getapp(),
                    device.isPushEnabled(),
                    device.getLastSeenAt(),
                    device.getCreatedAt(),
                    device.getUpdatedAt()
            );
        }
    }
}

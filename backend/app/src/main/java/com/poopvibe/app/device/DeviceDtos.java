package com.poopvibe.app.device;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import java.time.Instant;

/**
 * Request and response DTOs for device registration APIs.
 */
public final class DeviceDtos {
    private DeviceDtos() {
    }

    /**
     * Browser subscription keys required to encrypt Web Push payloads.
     */
    public record PushKeys(
            @NotBlank @Size(max = 255) String p256dh,
            @NotBlank @Size(max = 255) String auth
    ) {
    }

    /**
     * Request body used to register or refresh a browser push subscription.
     */
    public record RegisterDeviceRequest(
            @NotNull Long userId,
            @NotBlank @Size(max = 2048) String endpoint,
            @Valid @NotNull PushKeys keys,
            @NotNull Deviceapp app,
            @Size(max = 500) String userAgent,
            boolean pushEnabled
    ) {
    }

    /**
     * Device registration returned to API callers.
     */
    public record DeviceResponse(
            Long id,
            Long userId,
            String endpoint,
            Deviceapp app,
            String userAgent,
            boolean pushEnabled,
            Instant lastSeenAt,
            Instant lastPushSuccessAt,
            Instant lastPushFailureAt,
            String lastPushFailureReason,
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
                    device.getEndpoint(),
                    device.getapp(),
                    device.getUserAgent(),
                    device.isPushEnabled(),
                    device.getLastSeenAt(),
                    device.getLastPushSuccessAt(),
                    device.getLastPushFailureAt(),
                    device.getLastPushFailureReason(),
                    device.getCreatedAt(),
                    device.getUpdatedAt()
            );
        }
    }
}

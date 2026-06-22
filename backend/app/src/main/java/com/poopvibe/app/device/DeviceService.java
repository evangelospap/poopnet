package com.poopvibe.app.device;

import com.poopvibe.app.activity.ActivityLogService;
import com.poopvibe.app.activity.ActivityType;
import com.poopvibe.app.device.DeviceDtos.DeviceResponse;
import com.poopvibe.app.device.DeviceDtos.RegisterDeviceRequest;
import com.poopvibe.app.user.User;
import com.poopvibe.app.user.UserService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles idempotent device and push-token registration.
 */
@Service
public class DeviceService {
    private final DeviceRepository repository;
    private final UserService userService;
    private final ActivityLogService activityLogService;

    /**
     * Creates the device service.
     *
     * @param repository device repository
     * @param userService user lookup service
     * @param activityLogService activity logger
     */
    public DeviceService(DeviceRepository repository, UserService userService, ActivityLogService activityLogService) {
        this.repository = repository;
        this.userService = userService;
        this.activityLogService = activityLogService;
    }

    /**
     * Registers a device token or refreshes the existing token row.
     *
     * @param request device registration request
     * @return registered or refreshed device
     */
    @Transactional
    public DeviceResponse register(RegisterDeviceRequest request) {
        User user = userService.findEntity(request.userId());
        Device device = repository.findByFcmToken(request.fcmToken())
                .map(existing -> {
                    existing.refresh(request.app(), request.pushEnabled());
                    return existing;
                })
                .orElseGet(() -> new Device(user, request.fcmToken(), request.app(), request.pushEnabled()));
        Device saved = repository.save(device);
        activityLogService.record(user.getId(), ActivityType.DEVICE_REGISTERED, "device", saved.getId(), saved.getapp().name());
        return DeviceResponse.from(saved);
    }

    /**
     * Lists registered devices for a user.
     *
     * @param userId owner user identifier
     * @return user's registered devices
     */
    @Transactional(readOnly = true)
    public List<DeviceResponse> listForUser(Long userId) {
        userService.findEntity(userId);
        return repository.findByUserId(userId).stream().map(DeviceResponse::from).toList();
    }
}

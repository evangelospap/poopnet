package com.poopvibe.app.device;

import com.poopvibe.app.device.DeviceDtos.DeviceResponse;
import com.poopvibe.app.device.DeviceDtos.RegisterDeviceRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API for PWA device and push-token registration.
 */
@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService service;

    /**
     * Registers or refreshes a device token.
     *
     * @param request registration request
     * @return registered device
     */
    @PostMapping
    public ResponseEntity<DeviceResponse> register(@Valid @RequestBody RegisterDeviceRequest request) {
        DeviceResponse response = service.register(request);
        return ResponseEntity.created(URI.create("/api/v1/devices/" + response.id())).body(response);
    }

    /**
     * Lists devices for a user.
     *
     * @param userId owner user identifier
     * @return registered devices
     */
    @GetMapping
    public List<DeviceResponse> listForUser(@RequestParam Long userId) {
        return service.listForUser(userId);
    }
}

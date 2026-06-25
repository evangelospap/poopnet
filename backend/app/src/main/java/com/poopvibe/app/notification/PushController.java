package com.poopvibe.app.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API for browser push configuration discovery.
 */
@RestController
@RequestMapping("/api/v1/push")
@RequiredArgsConstructor
public class PushController {
    private final VapidKeyService vapidKeyService;

    /**
     * Returns the VAPID public key used by PushManager.subscribe.
     *
     * @return public key response
     */
    @GetMapping("/public-key")
    public PublicKeyResponse publicKey() {
        return new PublicKeyResponse(vapidKeyService.publicKey());
    }

    /**
     * Public VAPID key response for frontend subscription setup.
     */
    public record PublicKeyResponse(String publicKey) {
    }
}

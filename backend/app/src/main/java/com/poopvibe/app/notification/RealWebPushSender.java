package com.poopvibe.app.notification;

import com.poopvibe.app.device.Device;
import java.nio.charset.StandardCharsets;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Service;

/**
 * Delivers encrypted Web Push payloads through browser vendor push endpoints.
 */
@Service
public class RealWebPushSender implements WebPushSender {
    private final PushProperties properties;
    private final PushService pushService;

    /**
     * Creates a sender using the runtime VAPID key material.
     *
     * @param properties push delivery properties
     * @param vapidKeyService VAPID key resolver
     */
    public RealWebPushSender(PushProperties properties, VapidKeyService vapidKeyService) {
        this.properties = properties;
        VapidKeyMaterial keys = vapidKeyService.keyMaterial();
        try {
            this.pushService = new PushService(keys.publicKey(), keys.privateKey(), properties.getSubject());
        } catch (Exception ex) {
            throw new IllegalStateException("Could not initialize Web Push sender.", ex);
        }
    }

    /**
     * Sends one encrypted push payload to a browser endpoint.
     *
     * @param device browser subscription target
     * @param message notification payload
     * @return delivery result
     */
    @Override
    public PushSendResult send(Device device, PushMessage message) {
        try {
            byte[] payload = toJson(message).getBytes(StandardCharsets.UTF_8);
            Notification notification = new Notification(
                    device.getEndpoint(),
                    device.getP256dhKey(),
                    device.getAuthKey(),
                    payload,
                    properties.getTtlSeconds()
            );
            HttpResponse response = pushService.send(notification);
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                return PushSendResult.success("HTTP " + status);
            }
            return PushSendResult.failure(status == 404 || status == 410, "HTTP " + status);
        } catch (Exception ex) {
            return PushSendResult.failure(false, ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
    }

    private String toJson(PushMessage message) {
        return """
                {"title":"%s","body":"%s","url":"%s","icon":"%s","tag":"%s"}\
                """.formatted(
                escapeJson(message.title()),
                escapeJson(message.body()),
                escapeJson(message.url()),
                escapeJson(message.icon()),
                escapeJson(message.tag())
        );
    }

    private String escapeJson(String value) {
        return value == null ? "" : value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}

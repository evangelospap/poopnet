package com.poopvibe.app.notification;

import com.poopvibe.app.activity.ActivityLogService;
import com.poopvibe.app.activity.ActivityType;
import com.poopvibe.app.device.Device;
import com.poopvibe.app.device.DeviceService;
import com.poopvibe.app.friendship.Friendship;
import com.poopvibe.app.friendship.FriendshipService;
import com.poopvibe.app.session.PoopSession;
import com.poopvibe.app.session.SessionPrivacy;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 * Fans out session-finished push notifications to accepted friends.
 */
@Service
public class NotificationService {
    private final FriendshipService friendshipService;
    private final DeviceService deviceService;
    private final WebPushSender webPushSender;
    private final ActivityLogService activityLogService;
    private final Counter pushSentCounter;
    private final Counter pushFailedCounter;
    private final Counter pushSkippedCounter;

    /**
     * Creates the notification fanout service.
     *
     * @param friendshipService friendship lookup service
     * @param deviceService browser subscription service
     * @param webPushSender Web Push delivery adapter
     * @param activityLogService activity logger
     * @param meterRegistry metric registry
     */
    public NotificationService(
            FriendshipService friendshipService,
            DeviceService deviceService,
            WebPushSender webPushSender,
            ActivityLogService activityLogService,
            MeterRegistry meterRegistry
    ) {
        this.friendshipService = friendshipService;
        this.deviceService = deviceService;
        this.webPushSender = webPushSender;
        this.activityLogService = activityLogService;
        this.pushSentCounter = Counter.builder("poop_vibe_push_sent_total")
                .description("Total session-finished push notifications sent")
                .register(meterRegistry);
        this.pushFailedCounter = Counter.builder("poop_vibe_push_failed_total")
                .description("Total session-finished push notification failures")
                .register(meterRegistry);
        this.pushSkippedCounter = Counter.builder("poop_vibe_push_skipped_total")
                .description("Total session-finished push notification skips")
                .register(meterRegistry);
    }

    /**
     * Sends session-finished notifications for a newly created non-private session.
     *
     * @param session saved session
     * @param newlyCreated whether the request created the session instead of syncing an existing client ID
     */
    public void notifyFriendsSessionFinished(PoopSession session, boolean newlyCreated) {
        if (!newlyCreated) {
            recordSkip(session, "duplicate-client-id");
            return;
        }
        if (session.getPrivacy() == SessionPrivacy.PRIVATE) {
            recordSkip(session, "private-session");
            return;
        }

        Set<Long> recipientIds = acceptedFriendIds(session);
        if (recipientIds.isEmpty()) {
            recordSkip(session, "no-accepted-friends");
            return;
        }

        List<Device> devices = deviceService.enabledDevicesForUsers(recipientIds);
        if (devices.isEmpty()) {
            recordSkip(session, "no-enabled-subscriptions");
            return;
        }

        PushMessage message = new PushMessage(
                session.getUser().getUsername() + " finished a poop vibe 💩",
                "Tap to check the vibe.",
                "/friends",
                "/favicon.ico",
                "poop-vibe-session-" + session.getId()
        );

        for (Device device : devices) {
            PushSendResult result = webPushSender.send(device, message);
            if (result.successful()) {
                deviceService.recordPushSuccess(device);
                pushSentCounter.increment();
                activityLogService.recordActivity(session.getUser().getId(), ActivityType.PUSH_SENT, "device", device.getId(), result.detail());
            } else {
                deviceService.recordPushFailure(device, result.detail(), result.expired());
                pushFailedCounter.increment();
                activityLogService.recordActivity(session.getUser().getId(), ActivityType.PUSH_FAILED, "device", device.getId(), result.detail());
            }
        }
    }

    private Set<Long> acceptedFriendIds(PoopSession session) {
        Long actorId = session.getUser().getId();
        Set<Long> recipientIds = new HashSet<>();
        for (Friendship friendship : friendshipService.acceptedForUser(actorId)) {
            Long requesterId = friendship.getRequester().getId();
            Long addresseeId = friendship.getAddressee().getId();
            recipientIds.add(requesterId.equals(actorId) ? addresseeId : requesterId);
        }
        return recipientIds;
    }

    private void recordSkip(PoopSession session, String reason) {
        pushSkippedCounter.increment();
        activityLogService.recordActivity(session.getUser().getId(), ActivityType.PUSH_SKIPPED, "session", session.getId(), reason);
    }
}

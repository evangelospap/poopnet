package com.poopvibe.app.notification;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.poopvibe.app.activity.ActivityLogService;
import com.poopvibe.app.device.Device;
import com.poopvibe.app.device.DeviceService;
import com.poopvibe.app.device.Deviceapp;
import com.poopvibe.app.friendship.Friendship;
import com.poopvibe.app.friendship.FriendshipService;
import com.poopvibe.app.session.ComfortLevel;
import com.poopvibe.app.session.PoopSession;
import com.poopvibe.app.session.SessionMood;
import com.poopvibe.app.session.SessionPrivacy;
import com.poopvibe.app.user.User;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    @Mock
    private FriendshipService friendshipService;

    @Mock
    private DeviceService deviceService;

    @Mock
    private WebPushSender webPushSender;

    @Mock
    private ActivityLogService activityLogService;

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService(
                friendshipService,
                deviceService,
                webPushSender,
                activityLogService,
                new SimpleMeterRegistry()
        );
    }

    @Test
    void doesNotSendForDuplicateClientIdSync() {
        PoopSession session = session(SessionPrivacy.FRIENDS);

        notificationService.notifyFriendsSessionFinished(session, false);

        verify(webPushSender, never()).send(any(), any());
        verify(deviceService, never()).enabledDevicesForUsers(any());
    }

    @Test
    void doesNotSendForPrivateSessions() {
        PoopSession session = session(SessionPrivacy.PRIVATE);

        notificationService.notifyFriendsSessionFinished(session, true);

        verify(webPushSender, never()).send(any(), any());
        verify(deviceService, never()).enabledDevicesForUsers(any());
    }

    @Test
    void sendsToAcceptedFriendDevices() {
        PoopSession session = session(SessionPrivacy.FRIENDS);
        User friend = user(1002L, "Alex");
        Friendship friendship = new Friendship(session.getUser(), friend);
        Device device = new Device(
                friend,
                "https://push.example/subscription",
                "p256dh",
                "auth",
                Deviceapp.WEB,
                "JUnit",
                true
        );
        when(friendshipService.acceptedForUser(1001L)).thenReturn(List.of(friendship));
        when(deviceService.enabledDevicesForUsers(any())).thenReturn(List.of(device));
        when(webPushSender.send(any(), any())).thenReturn(PushSendResult.success("HTTP 201"));

        notificationService.notifyFriendsSessionFinished(session, true);

        verify(webPushSender).send(any(Device.class), any(PushMessage.class));
        verify(deviceService).recordPushSuccess(device);
    }

    private PoopSession session(SessionPrivacy privacy) {
        Instant endTime = Instant.parse("2026-06-25T10:00:00Z");
        return new PoopSession(
                UUID.randomUUID(),
                user(1001L, "Evangelos"),
                endTime.minusSeconds(180),
                endTime,
                SessionMood.LIGHT,
                ComfortLevel.OKAY,
                privacy,
                "Feeling light",
                false
        );
    }

    private User user(Long id, String username) {
        User user = org.mockito.Mockito.mock(User.class);
        when(user.getId()).thenReturn(id);
        lenient().when(user.getUsername()).thenReturn(username);
        return user;
    }
}

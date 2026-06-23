package com.poopvibe.app.activity;

/**
 * Domain events stored in the activity log and exposed to operators.
 */
public enum ActivityType {
    USER_CREATED,
    USER_UPDATED,
    DEVICE_REGISTERED,
    FRIEND_REQUESTED,
    FRIENDSHIP_UPDATED,
    SESSION_CREATED,
    SESSION_UPDATED,
    SESSION_DELETED,
    REACTION_ADDED,
    COMMENT_ADDED,
    MEDIA_ADDED
}

package com.azilen.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum NotificationSubEventType {
    CAMPAIGN_LAUNCH(1,"Campaign Launch"),
    CAMPAIGN_REMINDER(2, "Campaign Reminder"),
    RESET_PASSWORD(3, "Reset Password"),
    REGISTER_USER(4, "Register User"),
    ;

    private final int value;
    private String displayName;

    private static final Map<Integer, NotificationSubEventType> map = new HashMap<>();

    NotificationSubEventType(final int value, final String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    static {
        for (NotificationSubEventType en : NotificationSubEventType.values()) {
            map.put(en.value, en);
        }
    }

    public static NotificationSubEventType valueOf(int value) {
        return map.get(value);
    }

    public int getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

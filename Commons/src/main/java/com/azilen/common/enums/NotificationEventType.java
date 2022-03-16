package com.azilen.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum NotificationEventType {
    EMAIL(1,"Email"),
    SMS(2, "SMS"),
    ;

    private final int value;
    private String displayName;

    private static final Map<Integer, NotificationEventType> map = new HashMap<>();

    NotificationEventType(final int value, final String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    static {
        for (NotificationEventType en : NotificationEventType.values()) {
            map.put(en.value, en);
        }
    }

    public static NotificationEventType valueOf(int value) {
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

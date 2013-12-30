package com.engagepoint.teama.cmismanager.common.model;

public enum CardinalityEnum {
    SINGLE("single"), MULTI("multi");
    private final String value;

    private CardinalityEnum(String v) {
        value = v;
    }

    public static CardinalityEnum fromValue(String v) {
        for (CardinalityEnum c : CardinalityEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    @Override
    public String toString() {
        return value;
    }
}

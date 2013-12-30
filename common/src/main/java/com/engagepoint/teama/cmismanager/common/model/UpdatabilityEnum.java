package com.engagepoint.teama.cmismanager.common.model;

public enum UpdatabilityEnum {

    READONLY("readonly"), READWRITE("readwrite"), WHENCHECKEDOUT("whencheckedout"), ONCREATE("oncreate");
    private final String value;

    private UpdatabilityEnum(String v) {
        value = v;
    }

    public static UpdatabilityEnum fromValue(String v) {
        for (UpdatabilityEnum c : UpdatabilityEnum.values()) {
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

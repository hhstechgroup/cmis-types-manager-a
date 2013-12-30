package com.engagepoint.teama.cmismanager.common.model;

import java.io.Serializable;

public enum PropertyTypeEnum implements Serializable {

    BOOLEAN("boolean"), ID("id"), INTEGER("integer"), DATETIME("datetime"),
    DECIMAL("decimal"), HTML("html"), STRING("string"), URI("uri");

    private final String value;

    private PropertyTypeEnum(String propertyType) {
        value = propertyType;
    }

    public static PropertyTypeEnum fromValue(String propertyType) {
        for (PropertyTypeEnum c : PropertyTypeEnum.values()) {
            if (c.value.equals(propertyType)) {
                return c;
            }
        }
        throw new IllegalArgumentException(propertyType);
    }

    @Override
    public String toString() {
        return value;
    }
}
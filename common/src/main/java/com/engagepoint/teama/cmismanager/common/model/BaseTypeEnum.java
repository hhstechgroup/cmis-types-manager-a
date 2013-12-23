package com.engagepoint.teama.cmismanager.common.model;

public enum BaseTypeEnum {

    CMIS_DOCUMENT("cmis:document"),
    CMIS_FOLDER("cmis:folder"),
    CMIS_RELATIONSHIP("cmis:relationship"),
    CMIS_POLICY("cmis:policy"),
    CMIS_ITEM("cmis:item"),
    CMIS_SECONDARY("cmis:secondary");

    private final String baseTypeId;

    private BaseTypeEnum(String baseTypeId) {
        this.baseTypeId = baseTypeId;
    }

    public String getValue() {
        return baseTypeId;
    }

    @Override
    public String toString() {
        return baseTypeId;
    }

    public static BaseTypeEnum fromValue(String value) {
        for (BaseTypeEnum baseTypeEnum : BaseTypeEnum.values()) {
            if (baseTypeEnum.getValue().equals(value)) {
                return baseTypeEnum;
            }
        }
        throw new IllegalArgumentException(value);
    }
}

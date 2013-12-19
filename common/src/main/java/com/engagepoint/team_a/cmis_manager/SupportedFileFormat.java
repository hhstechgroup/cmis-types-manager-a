package com.engagepoint.team_a.cmis_manager;

public enum SupportedFileFormat {

    XML(".xml"),
    JSON(".json");

    private final String supportedFileFormat;

    private SupportedFileFormat(String supportedFileFormat) {
        this.supportedFileFormat = supportedFileFormat;
    }

    public String getValue() {
        return supportedFileFormat;
    }

    public static SupportedFileFormat fromValue(String value) {
        for (SupportedFileFormat supportedFileFormat : SupportedFileFormat.values()) {
            if (supportedFileFormat.getValue().equals(value)) {
                return supportedFileFormat;
            }
        }
        throw new IllegalArgumentException(value);
    }
}

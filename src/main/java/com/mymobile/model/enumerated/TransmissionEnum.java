package com.mymobile.model.enumerated;

public enum TransmissionEnum {
    MT("MANUAL","Manual"),
    AT("AUTOMATIC", "Automatic"),
    AM("AUTOMATED MANUAL", "Automated manual"),
    CVT("CONTINUOUSLY VARIABLE", "Continuously variable");

    private final String fullName;
    private final String displayName;

    TransmissionEnum(String fullName, String displayName) {
        this.fullName = fullName;
        this.displayName = displayName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
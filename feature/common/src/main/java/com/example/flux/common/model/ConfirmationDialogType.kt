package com.example.flux.common.model

enum class ConfirmationDialogType {
    SettingsDeleteDictionary,
    TicketCancel,
    Unknown,
    ;

    companion object {

        fun fromOrdinal(ordinal: Int): ConfirmationDialogType {
            for (type in values()) {
                if (type.ordinal == ordinal) {
                    return type
                }
            }
            return Unknown
        }
    }
}

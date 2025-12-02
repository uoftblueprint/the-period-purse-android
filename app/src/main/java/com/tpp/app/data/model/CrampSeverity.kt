package com.tpp.app.data.model

enum class CrampSeverity(val displayName: String) {
    Neutral("Neutral"),
    Bad("Bad"),
    Terrible("Terrible"),
    Good("Good"),
    None("None"),
    ;

    companion object {
        fun getSeverityByDisplayName(displayName: String): CrampSeverity? {
            return CrampSeverity.values().find { it.displayName == displayName }
        }
    }
}

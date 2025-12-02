package com.theperiodpurse.app.data.model

enum class Mood(val displayName: String) {
    HAPPY("Happy"),
    NEUTRAL("Meh"),
    SAD("Sad"),
    LOL("lol"), // Not implemented
    IDK("idk"), // Not implemented
    SILLY("Silly/Goofy"), // Different from iOS
    SICK("Sick"),
    ANGRY("Angry"),
    LOVED("Loved"),
    ;

    companion object {
        fun getMoodByDisplayName(displayName: String): Mood? {
            return Mood.values().find { it.displayName == displayName }
        }
    }
}

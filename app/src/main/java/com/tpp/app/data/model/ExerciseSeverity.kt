package com.theperiodpurse.app.data.model

enum class ExerciseSeverity {
    Little,
    Light,
    Medium,
    Heavy,
    ;

    companion object {
        fun fromMinutes(minutes: Int): ExerciseSeverity {
            return if (minutes > 60) {
                Heavy
            } else if (minutes >= 40) {
                Medium
            } else if (minutes >= 20) {
                Light
            } else {
                Little
            }
        }

        fun fromHoursAndMinutes(hours: Int, minutes: Int): ExerciseSeverity {
            return fromMinutes(hours * 60 + minutes)
        }
    }
}

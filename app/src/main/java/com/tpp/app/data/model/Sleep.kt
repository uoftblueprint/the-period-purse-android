package com.theperiodpurse.app.data.model

enum class Sleep {
    Little,
    Light,
    Medium,
    Heavy,
    ;

    companion object {
        fun fromMinutes(minutes: Int): Sleep {
            val sleepScore = minutes / 60
            return if (sleepScore > 10) {
                Heavy
            } else if (sleepScore >= 8) {
                Medium
            } else if (sleepScore >= 6) {
                Light
            } else {
                Little
            }
        }

        fun fromHoursAndMinutes(hours: Int, minutes: Int): Sleep {
            return fromMinutes(hours * 60 + minutes)
        }
    }
}

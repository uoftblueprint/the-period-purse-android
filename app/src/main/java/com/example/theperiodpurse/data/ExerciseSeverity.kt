package com.example.theperiodpurse.data

enum class ExerciseSeverity {
    Little,
    Light,
    Medium,
    Heavy;

    companion object {
        fun fromMinutes(minutes: Int): ExerciseSeverity {
            return if (minutes > 60) Heavy
            else if (minutes >= 40) Medium
            else if (minutes >= 20) Light
            else Little
        }
    }
}
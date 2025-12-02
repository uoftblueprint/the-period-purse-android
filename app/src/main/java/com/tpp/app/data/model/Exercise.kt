package com.tpp.app.data.model

enum class Exercise(val displayName: String) {
    CARDIO("Cardio"),
    YOGA("Yoga"),
    STRENGTH("Strength"),
    BALL_SPORTS("Ball Sports"),
    MARTIAL_ARTS("Martial Arts"),
    WATER_SPORTS("Water Sports"),
    CYCLING("Cycling"),
    RACKET_SPORTS("Racquet Sports"),
    WINTER_SPORTS("Winter Sports"),
    ;
    companion object {
        fun getExerciseByDisplayName(displayName: String): Exercise? {
            return Exercise.values().find { it.displayName == displayName }
        }
    }
}

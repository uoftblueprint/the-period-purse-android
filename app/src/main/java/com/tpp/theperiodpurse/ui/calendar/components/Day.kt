package com.tpp.theperiodpurse.ui.calendar.components

import androidx.compose.ui.graphics.Color
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.data.*
import com.tpp.theperiodpurse.ui.calendar.CalendarDayUIState
import com.kizitonwose.calendar.core.CalendarDay
import java.time.LocalDate


fun getDayColorAndIcon(
    day: CalendarDay,
    activeSymptom: Symptom,
    calendarDayUIState: CalendarDayUIState?
): Pair<Color, Int> {
    val defaultColor = Color.White
    val defaultImage = R.drawable.blank
    val default = Pair(defaultColor, defaultImage)
    if (day.date.isAfter(LocalDate.now())) {
        return Pair(Color(237, 237, 237), R.drawable.blank)
    }
    if (calendarDayUIState == null) {
        return default
    }
    return when (activeSymptom) {
        Symptom.FLOW ->
            when (calendarDayUIState.flow) {
                FlowSeverity.Light -> Pair(Color(215, 125, 125), R.drawable.water_drop_black_24dp)
                FlowSeverity.Medium -> Pair(Color(210, 80, 75), R.drawable.opacity_black_24dp)
                FlowSeverity.Heavy -> Pair(Color(195, 50, 50), R.drawable.flow_heavy)
                FlowSeverity.Spotting -> Pair(Color(245, 192, 192), R.drawable.spotting)
                FlowSeverity.None, null -> default
            }
        Symptom.CRAMPS ->
            when (calendarDayUIState.crampSeverity) {
                CrampSeverity.Bad -> Pair(
                    Color(0xFFDD8502),
                    R.drawable.sentiment_dissatisfied_black_24dp
                )
                CrampSeverity.Good -> Pair(
                    Color(0xFFFFD363),
                    R.drawable.sentiment_satisfied_black_24dp
                )
                CrampSeverity.Neutral -> Pair(
                    Color(0xFFE3797A),
                    R.drawable.sentiment_neutral_black_24dp
                )
                CrampSeverity.Terrible -> Pair(
                    Color(0xFFB85A04),
                    R.drawable.sentiment_very_dissatisfied_black_24dp
                )
                CrampSeverity.None, null -> default
            }
        Symptom.EXERCISE ->
            Pair(
                when (calendarDayUIState.exerciseSeverity) {
                    ExerciseSeverity.Little -> Color(0xFFB9E0D8)
                    ExerciseSeverity.Light -> Color(0xFF7BCFC0)
                    ExerciseSeverity.Medium -> Color(0xFF5A9F93)
                    ExerciseSeverity.Heavy -> Color(0xFF2F5B54)
                    null -> defaultColor
                },
                when (calendarDayUIState.exerciseType) {
                    Exercise.CARDIO -> R.drawable.round_directions_run_24
                    Exercise.YOGA -> R.drawable.self_improvement_black_24dp
                    Exercise.STRENGTH -> R.drawable.round_fitness_center_24
                    Exercise.BALL_SPORTS -> R.drawable.round_sports_soccer_24
                    Exercise.MARTIAL_ARTS -> R.drawable.round_sports_martial_arts_24
                    Exercise.WATER_SPORTS -> R.drawable.round_pool_24
                    Exercise.CYCLING -> R.drawable.round_directions_bike_24
                    Exercise.RACKET_SPORTS -> R.drawable.round_sports_tennis_24
                    Exercise.WINTER_SPORTS -> R.drawable.round_downhill_skiing_24
                    null -> R.drawable.blank
                }
            )
        Symptom.MOOD ->
            Pair(
                defaultColor,
                when (calendarDayUIState.mood) {
                    Mood.HAPPY -> R.drawable.sentiment_satisfied_black_24dp
                    Mood.NEUTRAL -> R.drawable.sentiment_neutral_black_24dp
                    Mood.SAD -> R.drawable.sentiment_dissatisfied_black_24dp
                    Mood.SILLY -> R.drawable.sentiment_very_satisfied_black_24dp
                    Mood.SICK -> R.drawable.sick_black_24dp
                    Mood.ANGRY -> R.drawable.round_mood_bad_24
                    Mood.LOVED -> R.drawable.round_favorite_24
                    else -> R.drawable.blank
                }
            )
        Symptom.SLEEP ->
            Pair(
                when (calendarDayUIState.sleepScore) {
                    Sleep.Little -> Color(0xFF92B8F0)
                    Sleep.Light -> Color(0xFF467CCD)
                    Sleep.Medium -> Color(0xFF1A50A0)
                    Sleep.Heavy -> Color(0xFF133364)
                    null -> defaultColor
                },
                R.drawable.blank
            )
    }
}
package com.tpp.theperiodpurse.ui.calendar

import androidx.compose.ui.graphics.Color
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.data.model.*
import com.tpp.theperiodpurse.ui.state.CalendarDayUIState
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel

fun getDayColorAndIcon(
    activeSymptom: Symptom,
    calendarDayUIState: CalendarDayUIState?,
    appViewModel: AppViewModel
): Pair<Color, Int> {
    val defaultColor = appViewModel.colorPalette.CalendarDayColor
    val defaultImage = R.drawable.blank
    val default = Pair(defaultColor, defaultImage)
    if (calendarDayUIState == null) {
        return default
    }
    return when (activeSymptom) {
        Symptom.FLOW -> flowOptions(calendarDayUIState, default)
        Symptom.CRAMPS -> crampOptions(calendarDayUIState, default)
        Symptom.EXERCISE ->
            Pair(exerciseSevirityOptions(calendarDayUIState, defaultColor), exerciseTypeOptions(calendarDayUIState))
        Symptom.MOOD ->
            Pair(defaultColor, moodOptions(calendarDayUIState))
        Symptom.SLEEP ->
            Pair(sleepScoreOptions(calendarDayUIState, defaultColor), R.drawable.blank)
        Symptom.OVULATION -> Pair(defaultColor, R.drawable.ovulation_egg_24dp)

    }
}

private fun flowOptions(calendarDayUIState: CalendarDayUIState, default: Pair<Color, Int>): Pair<Color, Int> {
    return when (calendarDayUIState.flow) {
        FlowSeverity.Light -> Pair(Color(0xFFD77D7D), R.drawable.water_drop_black_24dp)
        FlowSeverity.Medium -> Pair(Color(0xFFD2504B), R.drawable.opacity_black_24dp)
        FlowSeverity.Heavy -> Pair(Color(0xFFC33232), R.drawable.flow_heavy)
        FlowSeverity.Spotting -> Pair(Color(0xFFF5C0C0), R.drawable.spotting)
        FlowSeverity.Predicted -> Pair(Color(0xFFEEC6FE), R.drawable.blank)
        FlowSeverity.None, null -> default
    }
}

private fun crampOptions(calendarDayUIState: CalendarDayUIState, default: Pair<Color, Int>): Pair<Color, Int> {
    return when (calendarDayUIState.crampSeverity) {
        CrampSeverity.Bad -> Pair(
            Color(0xFFDD8502),
            R.drawable.sentiment_dissatisfied_black_24dp,
        )
        CrampSeverity.Good -> Pair(
            Color(0xFFFFD363),
            R.drawable.sentiment_satisfied_black_24dp,
        )
        CrampSeverity.Neutral -> Pair(
            Color(0xFFE3797A),
            R.drawable.sentiment_neutral_black_24dp,
        )
        CrampSeverity.Terrible -> Pair(
            Color(0xFFB85A04),
            R.drawable.sentiment_very_dissatisfied_black_24dp,
        )
        CrampSeverity.None, null -> default
    }
}

private fun exerciseSevirityOptions(calendarDayUIState: CalendarDayUIState, defaultColor: Color): Color {
    return when (calendarDayUIState.exerciseSeverity) {
        ExerciseSeverity.Little -> Color(0xFFB9E0D8)
        ExerciseSeverity.Light -> Color(0xFF7BCFC0)
        ExerciseSeverity.Medium -> Color(0xFF5A9F93)
        ExerciseSeverity.Heavy -> Color(0xFF2F5B54)
        null -> defaultColor
    }
}

private fun exerciseTypeOptions(calendarDayUIState: CalendarDayUIState): Int {
    return when (calendarDayUIState.exerciseType) {
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
}

private fun moodOptions(calendarDayUIState: CalendarDayUIState): Int {
    return when (calendarDayUIState.mood) {
        Mood.HAPPY -> R.drawable.sentiment_satisfied_black_24dp
        Mood.NEUTRAL -> R.drawable.sentiment_neutral_black_24dp
        Mood.SAD -> R.drawable.sentiment_dissatisfied_black_24dp
        Mood.SILLY -> R.drawable.sentiment_very_satisfied_black_24dp
        Mood.SICK -> R.drawable.sick_black_24dp
        Mood.ANGRY -> R.drawable.round_mood_bad_24
        Mood.LOVED -> R.drawable.round_favorite_24
        else -> R.drawable.blank
    }
}

private fun sleepScoreOptions(calendarDayUIState: CalendarDayUIState, defaultColor: Color): Color {
    return when (calendarDayUIState.sleepScore) {
        Sleep.Little -> Color(0xFF92B8F0)
        Sleep.Light -> Color(0xFF467CCD)
        Sleep.Medium -> Color(0xFF1A50A0)
        Sleep.Heavy -> Color(0xFF133364)
        null -> defaultColor
    }
}

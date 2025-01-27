package com.tpp.theperiodpurse.ui.state

import com.tpp.theperiodpurse.data.model.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class CalendarDayUIState(
    val flow: FlowSeverity?,
    val mood: Mood? = null,
    val exerciseLengthString: String = "",
    val exerciseType: Exercise? = null,
    val crampSeverity: CrampSeverity? = null,
    val sleepString: String = "",
    val ovulating: Boolean? = null,
) {
    private val formatter = DateTimeFormatter.ISO_LOCAL_TIME

    private val exerciseLength =
        if (exerciseLengthString != "") LocalTime.parse(exerciseLengthString, formatter) else null

    val exerciseSeverity = if (exerciseLength != null) {
        ExerciseSeverity.fromHoursAndMinutes(
            exerciseLength.hour,
            exerciseLength.minute,
        )
    } else {
        null
    }

    val sleep = if (sleepString != "") LocalTime.parse(sleepString, formatter) else null
    val sleepScore =
        if (sleep != null) Sleep.fromHoursAndMinutes(sleep.hour, sleep.minute) else null
}

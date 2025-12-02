package com.theperiodpurse.app.ui.state

import com.theperiodpurse.app.data.model.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class CalendarDayUIState(
    val flow: FlowSeverity?,
    val mood: Mood? = null,
    val exerciseLengthString: String = "",
    val exerciseType: Exercise? = null,
    val crampSeverity: CrampSeverity? = null,
    val sleepString: String = "",
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

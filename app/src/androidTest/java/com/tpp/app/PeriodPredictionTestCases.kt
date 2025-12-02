package com.tpp.app

import com.tpp.app.data.entity.Date
import com.tpp.app.data.model.CrampSeverity
import com.tpp.app.data.model.Exercise
import com.tpp.app.data.model.FlowSeverity
import com.tpp.app.data.model.Mood
import java.text.SimpleDateFormat

val periodHistoryEmpty = arrayListOf<Date>()

val periodHistoryUnsorted = arrayListOf(
    Date(
        date = SimpleDateFormat("dd/MM/yyyy").parse("03/01/2023")!!,
        flow = FlowSeverity.Light,
        mood = Mood.HAPPY,
        exerciseLength = null,
        exerciseType = Exercise.CARDIO,
        crampSeverity = CrampSeverity.None,
        sleep = null,
        notes = "",
    ),
    Date(
        date = SimpleDateFormat("dd/MM/yyyy").parse("01/01/2023")!!,
        flow = FlowSeverity.Light,
        mood = Mood.HAPPY,
        exerciseLength = null,
        exerciseType = Exercise.CARDIO,
        crampSeverity = CrampSeverity.None,
        sleep = null,
        notes = "",
    ),
    Date(
        date = SimpleDateFormat("dd/MM/yyyy").parse("04/01/2023")!!,
        flow = FlowSeverity.None,
        mood = Mood.HAPPY,
        exerciseLength = null,
        exerciseType = Exercise.CARDIO,
        crampSeverity = CrampSeverity.None,
        sleep = null,
        notes = "",
    ),
    Date(
        date = SimpleDateFormat("dd/MM/yyyy").parse("02/01/2023")!!,
        flow = FlowSeverity.Light,
        mood = Mood.HAPPY,
        exerciseLength = null,
        exerciseType = Exercise.CARDIO,
        crampSeverity = CrampSeverity.None,
        sleep = null,
        notes = "",
    ),
)

val periodHistoryOneCycle = arrayListOf(
    Date(
        date = SimpleDateFormat("dd/MM/yyyy").parse("01/01/2023")!!,
        flow = FlowSeverity.Light,
        mood = Mood.HAPPY,
        exerciseLength = null,
        exerciseType = Exercise.CARDIO,
        crampSeverity = CrampSeverity.None,
        sleep = null,
        notes = "",
    ),
    Date(
        date = SimpleDateFormat("dd/MM/yyyy").parse("02/01/2023")!!,
        flow = FlowSeverity.Light,
        mood = Mood.HAPPY,
        exerciseLength = null,
        exerciseType = Exercise.CARDIO,
        crampSeverity = CrampSeverity.None,
        sleep = null,
        notes = "",
    ),
    Date(
        date = SimpleDateFormat("dd/MM/yyyy").parse("03/01/2023")!!,
        flow = FlowSeverity.Light,
        mood = Mood.HAPPY,
        exerciseLength = null,
        exerciseType = Exercise.CARDIO,
        crampSeverity = CrampSeverity.None,
        sleep = null,
        notes = "",
    ),
)

val periodHistoryTwoCycles = arrayListOf(
    Date(
        date = SimpleDateFormat("dd/MM/yyyy").parse("01/01/2023")!!,
        flow = FlowSeverity.Light,
        mood = Mood.HAPPY,
        exerciseLength = null,
        exerciseType = Exercise.CARDIO,
        crampSeverity = CrampSeverity.None,
        sleep = null,
        notes = "",
    ),
    Date(
        date = SimpleDateFormat("dd/MM/yyyy").parse("02/01/2023")!!,
        flow = FlowSeverity.Light,
        mood = Mood.HAPPY,
        exerciseLength = null,
        exerciseType = Exercise.CARDIO,
        crampSeverity = CrampSeverity.None,
        sleep = null,
        notes = "",
    ),
    Date(
        date = SimpleDateFormat("dd/MM/yyyy").parse("01/02/2023")!!,
        flow = FlowSeverity.Light,
        mood = Mood.HAPPY,
        exerciseLength = null,
        exerciseType = Exercise.CARDIO,
        crampSeverity = CrampSeverity.None,
        sleep = null,
        notes = "",
    ),
)

// 01/01, 02/01,  04/01, 05/01, 06/01,  13/01
val periodHistoryThreeCycles = arrayListOf(
    Date(
        date = SimpleDateFormat("dd/MM/yyyy").parse("01/01/2023")!!,
        flow = FlowSeverity.Light,
        mood = Mood.HAPPY,
        exerciseLength = null,
        exerciseType = Exercise.CARDIO,
        crampSeverity = CrampSeverity.None,
        sleep = null,
        notes = "",
    ),
    Date(
        date = SimpleDateFormat("dd/MM/yyyy").parse("02/01/2023")!!,
        flow = FlowSeverity.Light,
        mood = Mood.HAPPY,
        exerciseLength = null,
        exerciseType = Exercise.CARDIO,
        crampSeverity = CrampSeverity.None,
        sleep = null,
        notes = "",
    ),
    Date(
        date = SimpleDateFormat("dd/MM/yyyy").parse("04/01/2023")!!,
        flow = FlowSeverity.Light,
        mood = Mood.HAPPY,
        exerciseLength = null,
        exerciseType = Exercise.CARDIO,
        crampSeverity = CrampSeverity.None,
        sleep = null,
        notes = "",
    ),
    Date(
        date = SimpleDateFormat("dd/MM/yyyy").parse("05/01/2023")!!,
        flow = FlowSeverity.Light,
        mood = Mood.HAPPY,
        exerciseLength = null,
        exerciseType = Exercise.CARDIO,
        crampSeverity = CrampSeverity.None,
        sleep = null,
        notes = "",
    ),
    Date(
        date = SimpleDateFormat("dd/MM/yyyy").parse("06/01/2023")!!,
        flow = FlowSeverity.Light,
        mood = Mood.HAPPY,
        exerciseLength = null,
        exerciseType = Exercise.CARDIO,
        crampSeverity = CrampSeverity.None,
        sleep = null,
        notes = "",
    ),
    Date(
        date = SimpleDateFormat("dd/MM/yyyy").parse("13/01/2023")!!,
        flow = FlowSeverity.Light,
        mood = Mood.HAPPY,
        exerciseLength = null,
        exerciseType = Exercise.CARDIO,
        crampSeverity = CrampSeverity.None,
        sleep = null,
        notes = "",
    ),
)

val currDate = java.util.Date(System.currentTimeMillis())
val halfMonthDate = java.util.Date(System.currentTimeMillis() - 15 * 24 * 60 * 60 * 1000)

val periodHistoryCurrentDate = arrayListOf(
    Date(
        date = currDate,
        flow = FlowSeverity.Light,
        mood = Mood.HAPPY,
        exerciseLength = null,
        exerciseType = Exercise.CARDIO,
        crampSeverity = CrampSeverity.None,
        sleep = null,
        notes = "",
    ),
)

val periodHistoryHalfMonth = arrayListOf(
    Date(
        date = halfMonthDate,
        flow = FlowSeverity.Light,
        mood = Mood.HAPPY,
        exerciseLength = null,
        exerciseType = Exercise.CARDIO,
        crampSeverity = CrampSeverity.None,
        sleep = null,
        notes = "",
    ),
)

val periodHistoryHalfMonthMultiple = arrayListOf(
    Date(
        date = SimpleDateFormat("dd/MM/yyyy").parse("04/01/2023")!!,
        flow = FlowSeverity.Light,
        mood = Mood.HAPPY,
        exerciseLength = null,
        exerciseType = Exercise.CARDIO,
        crampSeverity = CrampSeverity.None,
        sleep = null,
        notes = "",
    ),
    Date(
        date = halfMonthDate,
        flow = FlowSeverity.Light,
        mood = Mood.HAPPY,
        exerciseLength = null,
        exerciseType = Exercise.CARDIO,
        crampSeverity = CrampSeverity.None,
        sleep = null,
        notes = "",
    ),
)

val periodHistoryMultipleYears = arrayListOf(
    Date(
        date = SimpleDateFormat("dd/MM/yyyy").parse("03/01/2022")!!,
        flow = FlowSeverity.Light,
        mood = Mood.HAPPY,
        exerciseLength = null,
        exerciseType = Exercise.CARDIO,
        crampSeverity = CrampSeverity.None,
        sleep = null,
        notes = "",
    ),
    Date(
        date = SimpleDateFormat("dd/MM/yyyy").parse("03/01/2023")!!,
        flow = FlowSeverity.Light,
        mood = Mood.HAPPY,
        exerciseLength = null,
        exerciseType = Exercise.CARDIO,
        crampSeverity = CrampSeverity.None,
        sleep = null,
        notes = "",
    ),
)

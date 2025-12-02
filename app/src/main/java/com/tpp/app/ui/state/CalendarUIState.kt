package com.theperiodpurse.app.ui.state

import com.theperiodpurse.app.data.model.*
import java.time.LocalDate
import kotlin.collections.LinkedHashMap

data class CalendarUIState(
    var days: LinkedHashMap<LocalDate, CalendarDayUIState>,
    var selectedSymptom: Symptom,
)

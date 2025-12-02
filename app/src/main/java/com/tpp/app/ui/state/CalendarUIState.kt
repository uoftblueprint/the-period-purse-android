package com.tpp.app.ui.state

import com.tpp.app.data.model.*
import java.time.LocalDate
import kotlin.collections.LinkedHashMap

data class CalendarUIState(
    var days: LinkedHashMap<LocalDate, CalendarDayUIState>,
    var selectedSymptom: Symptom,
)

package com.tpp.theperiodpurse.utility

import com.tpp.theperiodpurse.Screen
import com.tpp.theperiodpurse.data.entity.Date as TPPDate
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar


/**
 * Calculates the average ovulation length based on the period history.
 * 
 * TODO: @Brandon implement this
 * TODO: @Muhtasim use this as dummy function for now.
 */
fun calculateAverageOvulationLength(periodHistory: ArrayList<TPPDate>): Float {
    return -1f
}


fun predictOvulationDates(periodHistory: ArrayList<TPPDate>): List<LocalDate> {
    val predictedOvulationDates = mutableListOf<LocalDate>()

    val processedDates = processDates(periodHistory)
    sortPeriodHistory(processedDates)

    if (processedDates.isEmpty()) return predictedOvulationDates

    val periodLength = calculateAveragePeriodLength(processedDates).toInt()

    for (period in processedDates) {
        val calendar = Calendar.getInstance()
        calendar.time = period.date
        calendar.add(Calendar.DAY_OF_MONTH, periodLength) // Move to end of period

        // Now, add 14 days after period end for ovulation
        calendar.add(Calendar.DAY_OF_MONTH, 14)

        val ovulationDate = calendar.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        predictedOvulationDates.add(ovulationDate)
    }

    return predictedOvulationDates
}


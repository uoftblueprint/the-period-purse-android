package com.tpp.theperiodpurse.utility

import java.time.temporal.ChronoUnit
import com.tpp.theperiodpurse.data.entity.Date as TPPDate


/**
 * Calculates the average ovulation length based on the period history.
 * 
 * TODO: @Brandon implement this
 * TODO: @Muhtasim use this as dummy function for now.
 */
fun calculateAverageOvulationLength(periodHistory: ArrayList<TPPDate>): Float {
    if (periodHistory.isEmpty()) return -1f

    // filter ovulation dates
    val ovulationDates = periodHistory
        .filter { it.ovulating == true }
        .map { it.date }
        .sorted()

    // no ovulation available
    if (ovulationDates.isEmpty()) return -1f

    // return 0 if you have 1 ovulation (since no interval)
    if (ovulationDates.size == 1) return 0f

    // Calculate the differences in days between consecutive ovulation dates
    val dayDifferences = ovulationDates.zipWithNext { first, second ->
        ChronoUnit.DAYS.between(first.toInstant(), second.toInstant())
    }

    // Calculate the average of the differences
    val averageDays = dayDifferences.average()

    return averageDays.toFloat()
}
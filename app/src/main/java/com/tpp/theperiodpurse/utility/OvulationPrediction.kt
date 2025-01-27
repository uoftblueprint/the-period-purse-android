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
    // Use the helper function to process ovulation dates
    val ovulationDates = processDatesForOvulation(periodHistory)

    // No ovulation dates found
    if (ovulationDates.isEmpty()) return -1f

    // Sort the ovulation dates by date
    val sortedOvulationDates = ovulationDates.sortedBy { it.date }

    val ovulationLengths = ArrayList<Int>()

    var currentDate = sortedOvulationDates[0].date
    var consecutiveDays = 1

    for (i in 1 until sortedOvulationDates.size) {
        val nextDate = addOneDay(currentDate)

        if (nextDate == sortedOvulationDates[i].date) {
            consecutiveDays += 1
        } else {
            ovulationLengths.add(consecutiveDays)
            consecutiveDays = 1
        }
        currentDate = sortedOvulationDates[i].date
    }

    // Add the last phase length
    ovulationLengths.add(consecutiveDays)

    // Calculate and return the average ovulation length
    return ovulationLengths.sum().toFloat() / ovulationLengths.size
}


fun processDatesForOvulation(dates: ArrayList<TPPDate>): ArrayList<TPPDate> {
    // Filter for dates where ovulating is true and remove duplicates
    val filteredDates = dates.filter { it.ovulating == true }.distinctBy { it.date }
    return ArrayList(filteredDates)
}

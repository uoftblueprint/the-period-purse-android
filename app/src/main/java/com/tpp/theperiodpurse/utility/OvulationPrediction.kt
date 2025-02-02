package com.tpp.theperiodpurse.utility

import com.tpp.theperiodpurse.data.model.Ovulation
import com.tpp.theperiodpurse.data.entity.Date as TPPDate
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar


/**
 * Calculates the average ovulation length based on user's logged data.
 *
 * Returns -1 if no ovulation data is logged.
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
    val filteredDates = dates.filter { it.ovulating == Ovulation.Ovulating }.distinctBy { it.date }
    return ArrayList(filteredDates)
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


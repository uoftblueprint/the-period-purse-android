package com.tpp.app.utility

import com.tpp.app.data.entity.Date as TPPDate
import com.tpp.app.data.model.FlowSeverity
import java.lang.Float.min
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt
import java.util.Date

val c: Calendar = Calendar.getInstance()

/**
 * Increases a Java date input by one day. Returns the new date.
 */
fun addOneDay(date: Date): Date {
    c.time = date
    c.add(Calendar.DATE, 1)
    return c.time
}

/**
 * Given a list of Dates, sort the Dates into lists of periods
 * Return a list containing sublists where each sublist is a period
 */
fun parseDatesIntoPeriods(periodHistory: ArrayList<TPPDate>): ArrayList<ArrayList<TPPDate>> {
    val processedDates = processDates(periodHistory)

    // each date can only exist once, maybe put put them in a ordered set?
    sortPeriodHistory(processedDates)
    val periods = ArrayList<ArrayList<TPPDate>>()
    var currPeriod = ArrayList<TPPDate>()

    if (processedDates.size == 0) {
        return periods
    }

    for (i in 0 until processedDates.size) {
        // check if next element in the array is within one day
        if (i == 0) {
            currPeriod.add(processedDates[i])
        } else {
            val date1 = processedDates[i].date
            val date2 = processedDates[i - 1].date
            if (date1 != null && date2 != null) {
                val diff = date1.time - date2.time
                if (diff <= 86400000) {
                    currPeriod.add(processedDates[i])
                } else {
                    periods.add(currPeriod)
                    currPeriod = ArrayList()
                    currPeriod.add(processedDates[i])
                }
            }
        }
    }
    periods.add(currPeriod)
    return periods
}

/**
 * Sorts an array list of Dates that removes the dates with no flow (i.e., filtering by dates
 * with periods), and sorting the list by incrementing date.
 */
fun sortPeriodHistory(processedDates: ArrayList<TPPDate>) {
    // Removes all dates with no flow or spotting.
    processedDates.removeAll { it.flow == FlowSeverity.None || it.flow == FlowSeverity.Spotting }

    // Sorts dates in ascending order.
    processedDates.sortWith {
            date1, date2 ->
        date1.date?.compareTo(date2.date) ?: 0
    }
}

/**
* Calculates the average period length given the user's period history. Refer to TPPDate.kt for each
* date's parameters.
* A period contains consecutive days where flow serverity is logged, with at most 1 day between
* each logged date.
* Returns the average period length, or -1 if the ArrayList is empty.
*
* Examples:
*     - Logs on Jan. 1 and Jan. 2 ==> 2 day period
*     - Logs on Jan. 1 to Jan. 3 ==> 3 day period
*     - Logs on Jan. 1 and Jan. 5 ==> 1 day period, 1 day period
*/
fun calculateAveragePeriodLength(periodHistory: ArrayList<TPPDate>): Float {
    val processedDates = processDates(periodHistory)

    sortPeriodHistory(processedDates)

    if (processedDates.isEmpty()) {
        return (-1).toFloat()
    }
    val periodLengths = ArrayList<Int>()

    var currDate = processedDates[0].date
    var consecutiveDays = 1

    for (i in 1 until processedDates.size) {
        val currDatePlusOne = currDate?.let { addOneDay(it) }

        if (currDatePlusOne == processedDates[i].date) {
            consecutiveDays += 1
        } else {
            periodLengths.add(consecutiveDays)
            consecutiveDays = 1
        }
        currDate = processedDates[i].date
    }
    periodLengths.add(consecutiveDays)

    return (periodLengths.sum().toFloat() / periodLengths.size)
}

/**
* Calculates the average cycle length given the user's period history. Refer to date.kt for each
* date's parameters.
* A cycle starts counting the first day of the period, and stops counting the day before the next
* period starts.
* If the ArrayList is empty, return -1. If there is only one recorded cycle, return -2.
* Otherwise, the average cycle length is returned.
*
* Examples:
*      - Period 1: Started Jan 1, Period 2: Started Feb 1 ==> 31 day cycle.
*      - Period 1: Started Jan 1, Period 2: Started Jan 4, Period 3: Started Jan 13
*                  ==> Cycles of 3 and 9 days; average cycle length is 6 days.
*/
fun calculateAverageCycleLength(periodHistory: ArrayList<TPPDate>): Float {
    val processedDates = processDates(periodHistory)
    sortPeriodHistory(processedDates)

    if (processedDates.size == 0) {
        return (-1).toFloat()
    }

    val cycleLengths = ArrayList<Int>()

    var currDate = processedDates[0].date
    var currDatePlusOne = currDate?.let { addOneDay(it) }

    for (i in 1 until processedDates.size) {
        if (currDatePlusOne == processedDates[i].date) {
            currDatePlusOne = currDatePlusOne?.let { addOneDay(it) }
        } else {
            // Takes the time and subtracts it, then divides to convert from milliseconds to days.

            if (processedDates[i].date != null) {
                val time = ((processedDates[i].date?.time ?: 0) - (currDate?.time ?: 0)) / 86400000
                cycleLengths.add(time.toInt())

                currDate = processedDates[i].date

                currDatePlusOne = currDate?.let { addOneDay(it) }
            }
        }
    }
    return if (cycleLengths.size == 0) {
        (-2).toFloat()
    } else {
        (cycleLengths.sum().toFloat() / cycleLengths.size)
    }
}

/**
 * Return the number of days since the last period
 */
fun calculateDaysSinceLastPeriod(periodHistory: ArrayList<TPPDate>): Long {
    val processedDates = processDates(periodHistory)
    if (processedDates.isEmpty()) {
        return 0
    }
    sortPeriodHistory(processedDates)
    val lastPeriodDate = processedDates[processedDates.size - 1].date?.time
    val currDate = Date.from(
        LocalDateTime.of(
            LocalDate.now(),
            LocalDateTime.MIN.toLocalTime(),
        ).atZone(ZoneId.systemDefault()).toInstant(),
    ).time
    if (lastPeriodDate != null) {
        return (currDate - lastPeriodDate) / 86400000
    }
    return 0
}

/**
 * Calculate the the sweeping angle for the progress circle
 * assume one month contains 31 days, use the equation:
 * (number of days since last cycle / 31) * 360f
 */
fun calculateArcAngle(periodHistory: ArrayList<TPPDate>): Float {
    val processedDates = processDates(periodHistory)
    val averageCycleLength = calculateAverageCycleLength(processedDates)
    if (averageCycleLength <= 0f) {
        return 340f * min(1f, calculateDaysSinceLastPeriod(processedDates) / 31f)
    }
    val resultedAngle = calculateDaysSinceLastPeriod(processedDates) / averageCycleLength
    return 340f * min(1f, resultedAngle)
}

/**
 * Given an arraylist containing sublists of periods, find and return a HashTable
 * where the keys are years, and the values are corresponding periods
 */
fun findYears(periods: ArrayList<ArrayList<TPPDate>>): MutableMap<Int, ArrayList<ArrayList<TPPDate>>>? {
    val years = mutableMapOf<Int, ArrayList<ArrayList<TPPDate>>>()
    if (periods.size == 0 || periods[0].size == 0) {
        return null
    }
    for (period in periods) {
        val year = period[0].date?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()?.year
        if (years.containsKey(year)) {
            years[year]?.add(period)
        } else {
            if (year != null) {
                years[year] = ArrayList()
                years[year]?.add(period)
            }
        }
    }
    return years
}


/**
 * if there is no cycle, use 31 days
 */
fun getPeriodPrediction(periodHistory: ArrayList<TPPDate>): ArrayList<Date> {
    val processedDates = processDates(periodHistory)
    val periodLength = calculateAveragePeriodLength(processedDates)
    var cycleLength = calculateAverageCycleLength(processedDates)
    if (cycleLength == -2f) {
        cycleLength = 31f
    }

    val periods = parseDatesIntoPeriods(processedDates)
    val latestCycle = periods.last().first().date

    val dayInMilliseconds = 24 * 60 * 60 * 1000

    val timeToAdd = cycleLength * dayInMilliseconds // convert to milliseconds

    // start date of next period = start date of lastest period + cycleLength days
    val newTime = (latestCycle.time + timeToAdd).toLong()
    var newDate = Date(newTime)

    val dates = ArrayList<Date>()
    dates.add(newDate)

    // create an array of all consecutive predicted dates
    for (i in 1 until periodLength.roundToInt()) {
        newDate = Date(newDate.time + dayInMilliseconds)
        dates.add(newDate)
    }

    return dates
}

/**
 * Process the list so that dates are only
 * @param dates an arraylist of
 */

fun processDates(dates: ArrayList<TPPDate>): ArrayList<TPPDate> {
    // remove duplicate dates
    // remove dates that are predicted
    val filteredDates = dates.filterNot { it.flow == FlowSeverity.Predicted }
    return ArrayList(filteredDates.distinctBy { it.date })
}
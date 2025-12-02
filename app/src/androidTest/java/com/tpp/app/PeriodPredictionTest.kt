package com.theperiodpurse.app

import com.theperiodpurse.app.data.entity.*
import com.theperiodpurse.app.data.model.CrampSeverity
import com.theperiodpurse.app.data.model.Exercise
import com.theperiodpurse.app.data.model.FlowSeverity
import com.theperiodpurse.app.data.model.Mood
import com.theperiodpurse.app.utility.calculateArcAngle
import com.theperiodpurse.app.utility.calculateAverageCycleLength
import com.theperiodpurse.app.utility.calculateAveragePeriodLength
import com.theperiodpurse.app.utility.calculateDaysSinceLastPeriod
import com.theperiodpurse.app.utility.findYears
import com.theperiodpurse.app.utility.parseDatesIntoPeriods
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat

@HiltAndroidTest
class PeriodPredictionTest {

    @Test
    fun sortPeriodHistory() {
        val testedList = periodHistoryUnsorted
        com.theperiodpurse.app.utility.sortPeriodHistory(testedList)

        assertEquals(
            testedList,
            periodHistoryOneCycle,
        )
    }

    @Test
    fun calculatePeriodLength_EmptyList() {
        assertEquals(
            -1f,
            calculateAveragePeriodLength(periodHistoryEmpty),
        )
    }

    @Test
    fun calculatePeriodLength_Unsorted() {
        assertEquals(
            3f,
            calculateAveragePeriodLength(periodHistoryUnsorted),
        )
    }

    @Test
    fun calculatePeriodLength_OneCycle() {
        assertEquals(
            3f,
            calculateAveragePeriodLength(periodHistoryOneCycle),
        )
    }

    @Test
    fun calculatePeriodLength_TwoCycles() {
        assertEquals(
            1.5f,
            calculateAveragePeriodLength(periodHistoryTwoCycles),
        )
    }

    @Test
    fun calculatePeriodLength_ThreeCycles() {
        assertEquals(
            2f,
            calculateAveragePeriodLength(periodHistoryThreeCycles),
        )
    }

    @Test
    fun calculateCycleLength_EmptyList() {
        assertEquals(
            -1f,
            calculateAverageCycleLength(periodHistoryEmpty),
        )
    }

    @Test
    fun calculateCycleLength_OneCycle() {
        assertEquals(
            -2f,
            calculateAverageCycleLength(periodHistoryOneCycle),
        )
    }

    @Test
    fun calculateCycleLength_TwoCycles() {
        assertEquals(
            31f,
            calculateAverageCycleLength(periodHistoryTwoCycles),
        )
    }

    @Test
    fun calculateCycleLength_ThreeCycles() {
        assertEquals(
            6f,
            calculateAverageCycleLength(periodHistoryThreeCycles),
        )
    }

    @Test
    fun parseDatesIntoPeriods_NoCycles() {
        assertEquals(
            ArrayList<ArrayList<Date>>(),
            parseDatesIntoPeriods(periodHistoryEmpty),
        )
    }

    @Test
    fun parseDatesIntoPeriods_OneCycle() {
        assertEquals(
            arrayListOf(periodHistoryOneCycle),
            parseDatesIntoPeriods(periodHistoryOneCycle),
        )
    }

    @Test
    fun parseDatesIntoPeriods_MultipleCycles() {
        val expected = arrayListOf(
            arrayListOf(
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
            ),
            arrayListOf(
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
            ),
        )
        assertEquals(
            expected,
            parseDatesIntoPeriods(periodHistoryHalfMonthMultiple),
        )
    }

    @Test
    fun calculateDaysSinceLastPeriod_EmptyList() {
        assertEquals(
            0,
            calculateDaysSinceLastPeriod(periodHistoryEmpty),
        )
    }

    @Test
    fun calculateDaysSinceLastPeriod_CurrDate() {
        assertEquals(
            0,
            calculateDaysSinceLastPeriod(periodHistoryCurrentDate),
        )
    }

    @Test
    fun calculateDaysSinceLastPeriod_OneCycle() {
        assertEquals(
            14,
            calculateDaysSinceLastPeriod(periodHistoryHalfMonth),
        )
    }

    @Test
    fun calculateDaysSinceLastPeriod_MultipleCycles() {
        assertEquals(
            14,
            calculateDaysSinceLastPeriod(periodHistoryHalfMonthMultiple),
        )
    }

    @Test
    fun arcAngleCalculation_ZeroDays() {
        assertEquals(
            0f,
            calculateArcAngle(periodHistoryCurrentDate),
        )
    }

    @Test
    fun arcAngleCalculation() {
        assertEquals(
            340f * 14 / 31,
            calculateArcAngle(periodHistoryHalfMonth),
        )
    }

    @Test
    fun arcAngleCalculation_MaxDays() {
        assertEquals(
            340f,
            calculateArcAngle(periodHistoryOneCycle),
        )
    }

    @Test
    fun findYears_EmptyList() {
        val lst = parseDatesIntoPeriods(periodHistoryEmpty)
        assertEquals(
            null,
            findYears(lst),
        )
    }

    @Test
    fun findYears_OneCycle() {
        val map = findYears(parseDatesIntoPeriods(periodHistoryOneCycle))

        assertEquals(
            "[2023]",
            map?.keys.toString(),
        )

        assertEquals(
            1,
            map?.values?.toList()?.get(0)?.size,
        )
    }

    @Test
    fun findYears_MultipleCycles() {
        val map = findYears(parseDatesIntoPeriods(periodHistoryThreeCycles))

        assertEquals(
            "[2023]",
            map?.keys.toString(),
        )

        assertEquals(
            3,
            map?.values?.toList()?.get(0)?.size,
        )
    }

    @Test
    fun findYears_MultipleYears() {
        val map = findYears(parseDatesIntoPeriods(periodHistoryMultipleYears))

        assertEquals(
            "[2022, 2023]",
            map?.keys.toString(),
        )

        assertEquals(
            2,
            map?.values?.toList()?.size,
        )

        assertEquals(
            1,
            map?.values?.toList()?.get(0)?.size,
        )
    }
}

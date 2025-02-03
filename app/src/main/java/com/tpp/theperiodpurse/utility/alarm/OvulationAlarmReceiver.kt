package com.tpp.theperiodpurse.utility.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.time.Instant
import java.time.Duration
import java.util.ArrayList
import java.util.Date as JavaDate  // Alias java.util.Date

import com.tpp.theperiodpurse.data.entity.Date
import com.tpp.theperiodpurse.data.model.CrampSeverity
import com.tpp.theperiodpurse.data.model.FlowSeverity
import com.tpp.theperiodpurse.data.model.Mood
import com.tpp.theperiodpurse.data.model.Exercise
import com.tpp.theperiodpurse.utility.NotificationHelper

class OvulationAlarm : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmReceiver", "Ovulation alarm triggered! Preparing to send notification.")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val periodHistoryStrings = intent.getStringArrayListExtra("periodHistory")

            if (periodHistoryStrings.isNullOrEmpty()) {
                Log.e("AlarmReceiver", "Period history missing or empty!")
                return
            }

            // Convert `List<String>` back into `ArrayList<Date>`
            val periodHistory = ArrayList<Date>().apply {
                periodHistoryStrings.forEach { dateStr ->
                    val convertedDate = JavaDate.from(Instant.parse(dateStr)) // Convert to Java Date

                    // Create `Date` object with default values for missing parameters
                    add(
                        Date(
                            date = convertedDate,
                            flow = FlowSeverity.None,  // Default: No flow
                            mood = Mood.NEUTRAL,  // Default neutral mood
                            exerciseLength = Duration.ZERO,  // Default: No exercise
                            exerciseType = Exercise.YOGA,  // Default: No exercise type
                            crampSeverity = CrampSeverity.None,  // Default: No cramps
                            sleep = Duration.ZERO,  // Default: No sleep recorded
                            notes = "",  // Empty notes
                            ovulating = null  // Default: Not ovulating
                        )
                    )
                }
            }

            NotificationHelper.sendOvulationNotification(context, periodHistory)
            Log.d("AlarmReceiver", "Ovulation notification sent.")
        } else {
            Log.e("AlarmReceiver", "Skipping notification: Requires API 26+")
        }
    }
}

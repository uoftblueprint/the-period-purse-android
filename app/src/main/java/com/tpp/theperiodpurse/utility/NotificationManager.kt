package com.tpp.theperiodpurse.utility

import OvulationAlarm
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.data.entity.Date
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar

object NotificationHelper {

    private const val OVULATION_CHANNEL_ID = "ovulation_notifications"
    private const val OVULATION_CHANNEL_NAME = "Ovulation Notifications"

    fun createNotificationChannel(context: Context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                OVULATION_CHANNEL_ID,
                OVULATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            Log.d("NotificationManager", "Notification channel created: $OVULATION_CHANNEL_ID")
        } else {
            Log.d("NotificationManager", "No need to create notification channel for SDK < O.")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendOvulationNotification(context: Context, periodHistory: ArrayList<Date>) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Ensure notification channel exists
        createNotificationChannel(context)

        // Get predicted ovulation dates
        val predictedOvulationDates = predictOvulationDates(periodHistory)
        Log.d("NotificationManager", "Predicted Ovulation Dates: $predictedOvulationDates") // ADD THIS

        if (predictedOvulationDates.isEmpty()) {
            Log.e("NotificationManager", "No predicted ovulation dates found!")
            return
        }

        val today = LocalDate.now()
        Log.d("NotificationManager", "Today’s Date: $today") // ADD THIS

        if (predictedOvulationDates.contains(today)) {
            Log.d("NotificationManager", "Ovulation Day Matched! Sending Notification.") // ADD THIS

            val notification = NotificationCompat.Builder(context, OVULATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle("Ovulation Phase Reminder")
                .setContentText("Your ovulation phase has begun. Don't forget to log your symptoms!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(1, notification)
            Log.d("NotificationManager", "Ovulation notification sent for $today") // ADD THIS
        } else {
            Log.d("NotificationManager", "Today is NOT an ovulation day.") // ADD THIS
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun scheduleOvulationAlarms(context: Context, periodHistory: ArrayList<Date>) {
        val predictedOvulationDates = predictOvulationDates(periodHistory)
        if (predictedOvulationDates.isEmpty()) {
            Log.e("AlarmManager", "No ovulation dates available for alarm.")
            return
        }

        val nextOvulationDate = predictedOvulationDates.firstOrNull()
        if (nextOvulationDate == null) {
            Log.e("AlarmManager", "No valid ovulation date found")
            return
        }

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.YEAR, nextOvulationDate.year)
            set(Calendar.MONTH, nextOvulationDate.monthValue - 1) // Java Calendar is 0-based for months
            set(Calendar.DAY_OF_MONTH, nextOvulationDate.dayOfMonth)
            set(Calendar.HOUR_OF_DAY, 9)  // Set to 9 AM (adjustable)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, OvulationAlarm::class.java).apply {
            action = "com.tpp.theperiodpurse.OVULATION_NOTIFICATION"

            // Convert `periodHistory` dates to a `List<String>`
            val periodHistoryStrings = periodHistory.map { it.date.toInstant().toString() }
            putStringArrayListExtra("periodHistory", ArrayList(periodHistoryStrings))
        }
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        Log.d("AlarmManager", "Setting ovulation notification alarm for $nextOvulationDate at ${calendar.time}")

        if (alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        } else {
            Log.e("AlarmManager", "Cannot schedule exact alarms. App needs permission.")
        }
    }



    @RequiresApi(Build.VERSION_CODES.S)
    fun requestExactAlarmPermission(context: Context) {
        val intent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
        context.startActivity(intent)
    }
}

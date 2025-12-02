package com.theperiodpurse.app.utility.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.theperiodpurse.app.R
import java.util.*

class WeeklyAlarm : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent?) {
        try {
            println("reached Alarm Class")
            showNotification(context, "Weekly Reminder to Log Symptoms!", "This is your weekly reminder to log your symptoms")
            setAlarm(context)
        } catch (e: Exception) {
            println("didn't work rip")
            Log.d("Recieved an Exception", e.printStackTrace().toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun setAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, Alarm::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val hasAlarmPermission: Boolean = alarmManager.canScheduleExactAlarms()

        val calendar = Calendar.getInstance().apply {
//            timeInMillis = System.currentTimeMillis() + 604800000 // 1 week in milliseconds
            timeInMillis = System.currentTimeMillis() + 120000 // 1 week in milliseconds
        }
        if (hasAlarmPermission) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }

    private fun showNotification(context: Context, title: String, description: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelName = "notifications_channel"
        val channelId = "notifications_id"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        println("reached function")

        val builder = NotificationCompat.Builder(context, channelId)
            .setContentText(title)
            .setContentText(description)
            .setSmallIcon(R.drawable.app_logo)
        notificationManager.notify(1, builder.build())

        println("notification sent")
    }
}

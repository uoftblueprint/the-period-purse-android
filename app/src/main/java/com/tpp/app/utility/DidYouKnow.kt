package com.theperiodpurse.app.utility

import android.content.Context
import android.content.SharedPreferences
import com.theperiodpurse.app.R
import java.io.InputStream
import java.time.LocalDate

private const val SHARED_PREFS_NAME = "SharedPreferences"

/**
 * Reads and returns a random line of a text file based on the InputStream given.
 */
fun readRandomLine(inputStream: InputStream): String {
    return inputStream.bufferedReader().useLines {
        it.elementAtOrNull((0..28).random()) ?: ""
    }
}


/**
 * Gets and returns the SharedPreference based on the context of the environment.
 */
fun getSharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
}


/**
 * Changes the saved SharedPreference strings for FACT_KEY and DATE_KEY based on the current
 * date. If the stored value of the fact was saved on a different day, then both key values
 * are changed to a new random fact and the current date. Otherwise, the fact stays the same.
 */
fun setFact(context: Context) {
    val sharedPreferences = getSharedPreferences(context)
    val inputStream: InputStream = context.resources.openRawResource(R.raw.dykfacts)
    val c = LocalDate.now().toString()

    if (c != getLastDate(context)) {
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("FACT_KEY", readRandomLine(inputStream))
            putString("DATE_KEY", c)
        }.apply()
    }
}


/**
 * Returns the date of the current DATE_KEY value; i.e., the date that the current fact was set.
 */
fun getLastDate(context: Context): String? {
    val sharedPreferences = getSharedPreferences(context)
    return sharedPreferences.getString("DATE_KEY", null)
}


/**
 * Returns the date of the current FACT_KEY value.
 */
fun getFact(context: Context): String? {
    val sharedPreferences = getSharedPreferences(context)
    if (sharedPreferences.getString("FACT_KEY", null) == null) {
        setFact(context)
    }
    return sharedPreferences.getString("FACT_KEY", "")
}


/**
 * Given a string and max length, returns a new string up to the new length
 * followed by an ellipses.
 */
fun getSubString(s: String, maxLength: Int): String {
    return if (s.length >= maxLength) {
        s.substring(0, maxLength) + "..."
    }
    else {
        s
    }
}

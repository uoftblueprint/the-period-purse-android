package com.tpp.theperiodpurse.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tpp.theperiodpurse.R

enum class Symptom(@StringRes val nameId: Int, @DrawableRes val resourceId: Int) {
    MOOD(R.string.mood, R.drawable.sentiment_neutral_black_24dp),
    EXERCISE(R.string.exercise, R.drawable.self_improvement_black_24dp),
    CRAMPS(R.string.cramps, R.drawable.sick_black_24dp),
    SLEEP(R.string.sleep, R.drawable.nightlight_black_24dp),
    FLOW(R.string.period_flow, R.drawable.opacity_black_24dp),
    OVULATION(R.string.ovulation, R.drawable.ovulation_egg_24dp)
    ;

    companion object {
        private val map = values().associateBy { it.nameId }
        infix fun from(nameId: Int) = map[nameId]
    }
}

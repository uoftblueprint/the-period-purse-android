package com.tpp.app.ui.state

import android.accounts.Account
import com.tpp.app.data.entity.User
import java.time.LocalDate

data class OnboardUIState(
    /** Selected days quantity  */
    var days: Int = 0,
    /** Selected date for pickup (such as "Jan 1") */
    var date: String = "",
    /** Available Symptoms dates for the order*/
    var symptomsOptions: List<String> = listOf(),
    /** Available dates for the track*/
    var dateOptions: List<LocalDate> = listOf(),
    val user: User? = null,
    var googleAccount: Account? = null,
)

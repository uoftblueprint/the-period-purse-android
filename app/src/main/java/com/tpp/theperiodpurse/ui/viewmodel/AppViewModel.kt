package com.tpp.theperiodpurse.ui.viewmodel

import android.content.Context
import android.content.SharedPreferences

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpp.theperiodpurse.data.entity.Date
import com.tpp.theperiodpurse.data.model.Symptom
import com.tpp.theperiodpurse.data.repository.DateRepository
import com.tpp.theperiodpurse.data.repository.UserRepository
import com.tpp.theperiodpurse.ui.state.AppUiState
import com.tpp.theperiodpurse.ui.state.CalendarDayUIState
import com.tpp.theperiodpurse.ui.theme.ColorPalette
import com.tpp.theperiodpurse.ui.theme.DarkColorPaletteImpl
import com.tpp.theperiodpurse.ui.theme.LightColorPaletteImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.sql.Time
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dateRepository: DateRepository,
    private val context: Context,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    var colorPalette: ColorPalette = if (_uiState.value.darkMode == true){
        DarkColorPaletteImpl()
    } else {
        LightColorPaletteImpl()
    }
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()
    var databaseIsLoadedFromStorage: MutableLiveData<Boolean?> = MutableLiveData(null)
    // Initialize shared preferences
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    fun loadData(calendarViewModel: CalendarViewModel, context: Context) {
        val trackedSymptoms: MutableList<Symptom> = mutableListOf()
        viewModelScope.launch {
            // data base operations should be async
            val isUserRepositoryEmpty = userRepository.isEmpty(context)
            val databaseDates = dateRepository.getAllDates(context)
//            databaseIsLoadedFromStorage.postValue(true)
            if (!isUserRepositoryEmpty) {
                val user = userRepository.getUser(1, context)
                trackedSymptoms.add(Symptom.FLOW)
                trackedSymptoms.addAll(user.symptomsToTrack)
                _uiState.update { currentState ->
                    currentState.copy(
                        trackedSymptoms = trackedSymptoms,
                        allowReminders = user.allowReminders,
                        reminderFrequency = user.reminderFreq,
                        reminderTime = user.reminderTime,
                        darkMode = user.darkMode
                    )
                }
                setColorMode(darkMode = user.darkMode, context = context)
                databaseDates.collect { dates ->
                    updateUIDateState(dates, calendarViewModel)
                }
            }
        }
        databaseIsLoadedFromStorage.postValue(true)
    }

    private fun updateUIDateState(
        dates: List<Date>,
        calendarViewModel: CalendarViewModel,
    ) {
        _uiState.value = _uiState.value.copy(dates = dates)

        for (date in dates) {
            // for it convert correctly we subtract 19 hours worth of milliseconds
            var convertedExcLen = ""
            if (date.exerciseLength != null) {
                convertedExcLen = Time(
                    date.exerciseLength.toMillis() - 19 * 36 * 100000,
                ).toString()
            }

            var convertedSleepLen = ""
            if (date.sleep != null) {
                convertedSleepLen = Time(
                    date.sleep.toMillis() - 19 * 36 * 100000,
                ).toString()
            }


            calendarViewModel.setDayInfo(
                date.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                CalendarDayUIState(
                    flow = date.flow,
                    mood = date.mood,
                    exerciseLengthString = convertedExcLen,
                    exerciseType = date.exerciseType,
                    crampSeverity = date.crampSeverity,
                    sleepString = convertedSleepLen,
                ),
            )
        }
    }

    fun getTrackedSymptoms(): List<Symptom> {
        return uiState.value.trackedSymptoms
    }

    fun getDates(): List<Date> {
        return uiState.value.dates
    }

    fun toggleColorMode(context: Context) {
        val currentColorMode = _uiState.value.darkMode
        setColorMode(!currentColorMode, context)
    }

    fun setColorMode(darkMode: Boolean, context: Context) {
        colorPalette = if (darkMode) {
            DarkColorPaletteImpl()
        } else {
            LightColorPaletteImpl()
        }
        _uiState.update { currentState -> currentState.copy(darkMode = darkMode) }
        userRepository.setColorMode(darkMode, context)

    }

    fun getColorMode(): Boolean {
        return uiState.value.darkMode
    }

    fun setTrackedSymptoms(trackedSymptoms: ArrayList<Symptom>, context: Context) {
        _uiState.update { currentState ->
            currentState.copy(trackedSymptoms = trackedSymptoms)
        }
        val sympCopy = trackedSymptoms.toMutableList()
        sympCopy.remove(Symptom.FLOW)
        userRepository.setSymptoms(sympCopy, context)
    }

    fun updateSymptoms(symptom: Symptom, context: Context): Boolean {
        val symptoms = getTrackedSymptoms()
        val sympCopy = symptoms.toMutableList()
        if (symptoms.contains(symptom)) {
            sympCopy.remove(symptom)
            setTrackedSymptoms(sympCopy as ArrayList<Symptom>, context)
            return false
        } else {
            sympCopy.add(symptom)
            setTrackedSymptoms(sympCopy as ArrayList<Symptom>, context)
            return true
        }
    }

    fun isSymptomChecked(symptom: Symptom): Boolean {
        val symptoms = getTrackedSymptoms()
        return symptoms.contains(symptom)
    }

    fun getAllowReminders(): Boolean {
        return uiState.value.allowReminders
    }

    fun toggleAllowReminders(context: Context) {
        val currentReminderState = _uiState.value.allowReminders
        _uiState.update { currentState -> currentState.copy(allowReminders = !currentReminderState) }
        userRepository.setReminders(!currentReminderState, context)
    }

    fun getReminderTime(): String {
        return uiState.value.reminderTime
    }

    fun setReminderTime(time: String, context: Context) {
        _uiState.update { currentState -> currentState.copy(reminderTime = time) }
        userRepository.setReminderTime(time, context)
    }

    fun setReminderFreq(freq: String) {
        _uiState.update { currentState -> currentState.copy(reminderFrequency = freq) }
        userRepository.setReminderFreq(freq)
    }

    fun getReminderFreq(): String {
        return uiState.value.reminderFrequency
    }


    fun getOvulationNotificationEnabled(): Boolean {
        // Retrieve the state from shared preferences or database
        return sharedPreferences.getBoolean("ovulation_notifications", false)
    }

    fun setOvulationNotificationEnabled(enabled: Boolean) {
        // Save the state in shared preferences or database
        sharedPreferences.edit().putBoolean("ovulation_notifications", enabled).apply()
    }


    fun saveDate(date: Date, context: Context) {
        dateRepository.addDate(date, context)
        val newList = uiState.value.dates.toMutableList()
        newList.add(date)
        _uiState.update { currentState -> currentState.copy(dates = newList) }
    }

    fun deleteDate(date: Date, context: Context) {
        dateRepository.deleteDate(date, context)
        val newList = uiState.value.dates.toMutableList()
        newList.remove(date)
        _uiState.update { currentState -> currentState.copy(dates = newList) }
    }

    fun deleteManyDates(dates: List<java.util.Date>, context: Context) {
        val convertedDates = dates.map { it.time }
        viewModelScope.launch {
            dateRepository.deleteManyDates(convertedDates, context)
        }

        val newList = mutableListOf<Date>()

        for (date in uiState.value.dates) {
            if (!dates.contains(date.date)) {
                newList.add(date)
            }
        }

        _uiState.update { currentState -> currentState.copy(dates = newList) }
    }
}

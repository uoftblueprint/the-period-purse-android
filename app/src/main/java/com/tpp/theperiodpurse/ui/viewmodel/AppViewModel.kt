package com.tpp.theperiodpurse.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpp.theperiodpurse.data.*
import com.tpp.theperiodpurse.data.entity.Date
import com.tpp.theperiodpurse.data.model.Symptom
import com.tpp.theperiodpurse.data.repository.DateRepository
import com.tpp.theperiodpurse.data.repository.UserRepository
import com.tpp.theperiodpurse.ui.state.CalendarDayUIState
import com.tpp.theperiodpurse.ui.state.AppUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Time
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor (
    private val userRepository: UserRepository,
    private val dateRepository: DateRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()
    var isLoaded: MutableLiveData<Boolean?> = MutableLiveData(null)


    fun loadData(calendarViewModel: CalendarViewModel, context: Context) {
        val trackedSymptoms: MutableList<Symptom> = mutableListOf()
        viewModelScope.launch {
            withContext(Dispatchers.Main){
                ApplicationRoomDatabase.getDatabase(context)
            }
            Log.d(
                "Lookies Here",
                withContext(Dispatchers.Main) { !userRepository.isEmpty() }.toString()
            )
            if (withContext(Dispatchers.Main) { !userRepository.isEmpty() }) {
                val user = withContext(Dispatchers.Main) { userRepository.getUser(1) }
                trackedSymptoms.add(Symptom.FLOW)
                trackedSymptoms.addAll(user.symptomsToTrack)
                _uiState.update { currentState ->
                    currentState.copy(trackedSymptoms = trackedSymptoms,
                    allowReminders = user.allowReminders,
                    reminderFrequency = user.reminderFreq,
                    reminderTime = user.reminderTime,)
                }
            }
            withContext(Dispatchers.IO) {
                dateRepository.getAllDates().collect { dates ->
                    _uiState.value = _uiState.value.copy(dates = dates)

                    for (date in dates) {
                        // for it convert correctly we subtract 19 hours worth of milliseconds
                        var convertedExcLen = ""
                        if (date.exerciseLength != null){
                            convertedExcLen = Time(
                                date.exerciseLength.toMillis()-19*36*100000
                            ).toString()
                        }

                        var convertedSleepLen = ""
                        if (date.sleep != null) {
                            convertedSleepLen = Time(
                                date.sleep.toMillis()-19*36*100000
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
                                sleepString = convertedSleepLen
                            )
                        )
                    }
                }
            }


        }
        isLoaded.postValue(true)
    }

    fun getTrackedSymptoms() : List<Symptom> {
        return uiState.value.trackedSymptoms
    }

    fun getDates() : List<Date> {
        return uiState.value.dates
    }

    fun setTrackedSymptoms(trackedSymptoms: ArrayList<Symptom>) {
        _uiState.update { currentState ->
            currentState.copy(trackedSymptoms = trackedSymptoms)
        }
        val sympCopy = trackedSymptoms.toMutableList()
        sympCopy.remove(Symptom.FLOW)
        userRepository.setSymptoms(sympCopy)
    }

    fun updateSymptoms(symptom: Symptom): Boolean {
        val symptoms = getTrackedSymptoms()
        val sympCopy = symptoms.toMutableList()
        if(symptoms.contains(symptom)){
            sympCopy.remove(symptom)
            setTrackedSymptoms(sympCopy as ArrayList<Symptom>)
            return false
        } else {
            sympCopy.add(symptom)
            setTrackedSymptoms(sympCopy as ArrayList<Symptom>)
            return true
        }
    }

    fun isSymptomChecked(symptom: Symptom): Boolean{
        val symptoms = getTrackedSymptoms()
        return symptoms.contains(symptom)
    }

    fun getAllowReminders(): Boolean{
        return uiState.value.allowReminders
    }

    fun toggleAllowReminders(){
        val currentReminderState = _uiState.value.allowReminders
        _uiState.update { currentState -> currentState.copy(allowReminders = !currentReminderState)}
        userRepository.setReminders(!currentReminderState)
    }

    fun getReminderTime(): String{
        return uiState.value.reminderTime
    }

    fun setReminderTime(time: String){
        _uiState.update { currentState -> currentState.copy(reminderTime = time) }
        userRepository.setReminderTime(time)
    }

    fun setReminderFreq(freq: String){
        _uiState.update { currentState -> currentState.copy(reminderFrequency = freq) }
        userRepository.setReminderFreq(freq)
    }

    fun getReminderFreq(): String{
        return uiState.value.reminderFrequency
    }

    fun saveDate(date: Date) {
        dateRepository.addDate(date)
        val newList = uiState.value.dates.toMutableList()
        newList.add(date)
        _uiState.update { currentState -> currentState.copy(dates = newList) }

    }

    fun deleteDate(date: Date) {
        dateRepository.deleteDate(date)
        val newList = uiState.value.dates.toMutableList()
        newList.remove(date)
        _uiState.update { currentState -> currentState.copy(dates = newList) }

    }

    fun deleteManyDates(dates: List<java.util.Date>) {
        val convertedDates = dates.map { it.time }
        dateRepository.deleteManyDates(convertedDates)

        val newList = mutableListOf<Date>()

        for (date in uiState.value.dates) {
            if (!dates.contains(date.date)) {
                newList.add(date)
            }
        }

        _uiState.update { currentState -> currentState.copy(dates = newList) }



    }
}
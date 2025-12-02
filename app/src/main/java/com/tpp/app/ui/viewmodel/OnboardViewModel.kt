package com.tpp.app.ui.viewmodel
import android.accounts.Account
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import com.tpp.app.R
import com.tpp.app.data.*
import com.tpp.app.data.entity.Date
import com.tpp.app.data.entity.User
import com.tpp.app.data.model.Symptom
import com.tpp.app.data.repository.UserRepository
import com.tpp.app.ui.state.OnboardUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject

@HiltViewModel
class OnboardViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardUIState())
    val uiState: StateFlow<OnboardUIState> = _uiState.asStateFlow()

    var isOnboarded: LiveData<Boolean?> = userRepository.isOnboarded
    var isDeleted: MutableLiveData<Boolean?> = MutableLiveData(null)
    var googleDriveFolder: MutableLiveData<FileList?> = MutableLiveData(null)
    var drivePermissionHasError: MutableLiveData<Boolean?> = MutableLiveData(null)
    var googleDriveLoadSuccess: MutableLiveData<Boolean?> = MutableLiveData(null)
    var hasBackedUpToGoogleDrive: MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkGoogleLogin(context: Context): Boolean {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        return (account != null)
    }

    fun checkOnboardedStatus(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.isOnboarded(context)
            }
            isOnboarded = userRepository.isOnboarded
        }
    }
    fun checkDeletedStatus(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val instance = ApplicationRoomDatabase.getDatabase(context)
                instance.clearAllTables()
                isDeleted.postValue(true)
            }
        }
    }

    // TODO: go through all google drive opeartions and make sure usage of coroutine scope is
    //  correct
    fun checkGoogleDrive(account: Account, context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val credential = GoogleAccountCredential.usingOAuth2(
                        context,
                        listOf(
                            DriveScopes.DRIVE_FILE,
                            DriveScopes.DRIVE_APPDATA,
                        ),
                    )
                    credential.selectedAccount = account

                    val drive = Drive
                        .Builder(
                            AndroidHttp.newCompatibleTransport(),
                            JacksonFactory.getDefaultInstance(),
                            credential,
                        )
                        .setApplicationName(context.getString(R.string.app_name))
                        .build()

                    val googleDriveFolder = drive.files().list()
                        .setQ("name = 'user_database.db' and trashed = false")
                        .setSpaces("appDataFolder").execute()

                    this@OnboardViewModel.googleDriveFolder.postValue(googleDriveFolder)
                    drivePermissionHasError.postValue(null)
                } catch (e: Exception) {
                    Log.d("OnboardViewModel", e.toString())
                    googleDriveFolder.postValue(null)
                    drivePermissionHasError.postValue(true)
                }
            }
        }
    }

    fun downloadBackup(account: Account, context: Context) {
        viewModelScope.launch {
            ApplicationRoomDatabase.closeDatabase()
            val credential = GoogleAccountCredential.usingOAuth2(
                context,
                listOf(
                    DriveScopes.DRIVE_FILE,
                    DriveScopes.DRIVE_APPDATA,
                ),
            )
            credential.selectedAccount = account
            val drive = Drive
                .Builder(
                    AndroidHttp.newCompatibleTransport(),
                    JacksonFactory.getDefaultInstance(),
                    credential,
                )
                .setApplicationName(context.getString(R.string.app_name))
                .build()
            withContext(Dispatchers.IO) {
                val fileList = drive.files().list()
                    .setQ("name = 'user_database.db' and trashed = false")
                    .setSpaces("appDataFolder").execute()
                val fileId = fileList.files[0].id
                val outputStream = ByteArrayOutputStream()
                drive.files().get(fileId).executeMediaAndDownloadTo(outputStream)
                val data = outputStream.toByteArray()
                val dbFilePath = context.getDatabasePath("user_database.db").path
                FileOutputStream(dbFilePath).apply {
                    write(data)
                    flush()
                    close()
                }
            }
            // reopen database
            val database = ApplicationRoomDatabase.getDatabase(context)
            val userDAO = database.userDAO()
            withContext(Dispatchers.IO) {
                userDAO.getUsers()
            }
            googleDriveLoadSuccess.postValue(true)
        }
    }

    fun backupDatabase(account: Account, context: Context) {
        viewModelScope.launch {
            ApplicationRoomDatabase.closeDatabase()
            val credential = GoogleAccountCredential.usingOAuth2(
                context,
                listOf(
                    DriveScopes.DRIVE_APPDATA,
                    DriveScopes.DRIVE_FILE,
                ),
            )
            credential.selectedAccount = account
            val drive = Drive.Builder(
                AndroidHttp.newCompatibleTransport(),
                JacksonFactory.getDefaultInstance(),
                credential,
            ).setApplicationName(context.getString(R.string.app_name)).build()

            withContext(Dispatchers.IO) {
                val fileList = drive.files().list()
                    .setQ("name = 'user_database.db' and trashed = false")
                    .setSpaces("appDataFolder").execute()
                if (fileList.files.isNotEmpty()) {
                    val fileId = fileList.files[0].id
                    drive.files().delete(fileId).execute()
                }
            }

            val dbFile = java.io.File(context.getDatabasePath("user_database.db").absolutePath)
            val outputStream = ByteArrayOutputStream()
            val inputStream = withContext(Dispatchers.IO) {
                FileInputStream(dbFile)
            }
            inputStream.copyTo(outputStream)
            val metadata = File()
                .setParents(listOf("appDataFolder"))
                .setMimeType("application/x-sqlite3")
                .setName("user_database.db")
            withContext(Dispatchers.IO) {
                drive.files().create(metadata, ByteArrayContent(null, outputStream.toByteArray()))
                    .setFields("id")
                    .execute()
                inputStream.close()
                outputStream.close()
            }
            val newDatabase = ApplicationRoomDatabase.getDatabase(context)
            // check if database is open with a query
            withContext(Dispatchers.IO) {
                newDatabase.userDAO().getUsers()
            }
            hasBackedUpToGoogleDrive.postValue(true)
        }
    }

    /**
     * Set the quantity [averageDays] for average period length for this onboarding session
     */
    fun setQuantity(numberDays: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                days = numberDays,

            )
        }
    }

    /**
     * Set the [logSymptoms] to track for onboarding session.
     */
    fun setSymptoms(logSymptoms: String) {
        var listOfSymptoms: MutableList<String> = mutableListOf()
        var tempList = logSymptoms.split("|")
        tempList.forEach { option ->
            if (option != "") {
                listOfSymptoms.add(option)
            }
        }

        _uiState.update { currentState ->
            currentState.copy(symptomsOptions = listOfSymptoms)
        }
    }

    /**
     * Set the [lastDate] of last period for current onboarding session
     */
    fun setDate(startDate: String) {
        var dates = startDate.split("|")
        _uiState.update { currentState ->
            currentState.copy(
                date = dates[0] + " to " + dates[1],
            )
        }
    }

    private fun createUser(
        symptomsToTrack: ArrayList<Symptom>,
        periodHistory: ArrayList<Date>,
        averagePeriodLength: Int,
        averageCycleLength: Int,
        daysSinceLastPeriod: Int,
    ): User {
        return User(
            symptomsToTrack = symptomsToTrack,
            periodHistory = periodHistory,
            averagePeriodLength = averagePeriodLength,
            averageCycleLength = averageCycleLength,
            daysSinceLastPeriod = daysSinceLastPeriod,
        )
    }

    private fun saveUser(user: User, context: Context) {
        userRepository.addUser(user, context)
    }

    fun addNewUser(
        symptomsToTrack: ArrayList<Symptom>,
        periodHistory: ArrayList<Date>,
        averagePeriodLength: Int,
        averageCycleLength: Int,
        daysSinceLastPeriod: Int,
        context: Context,
    ) {
        saveUser(
            createUser(
                symptomsToTrack,
                periodHistory,
                averagePeriodLength,
                averageCycleLength,
                daysSinceLastPeriod,
            ),
            context,
        )
        viewModelScope.launch {
            val user = withContext(Dispatchers.Main) { userRepository.getUser(1, context) }
            _uiState.value = _uiState.value.copy(user = user)
        }
    }
}

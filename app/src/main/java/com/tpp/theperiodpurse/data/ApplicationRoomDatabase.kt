package com.tpp.theperiodpurse.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tpp.theperiodpurse.data.dao.DateDAO
import com.tpp.theperiodpurse.data.entity.Date
import com.tpp.theperiodpurse.data.entity.User
import com.tpp.theperiodpurse.data.helper.DateConverter
import com.tpp.theperiodpurse.data.helper.DaysConverter
import com.tpp.theperiodpurse.data.helper.DurationConverter
import com.tpp.theperiodpurse.data.helper.SymptomConverter
import java.io.File
import javax.inject.Singleton


@Database(entities = [User::class, Date::class], version = 8, exportSchema = true)
@Singleton
@TypeConverters(
    SymptomConverter::class,
    DateConverter::class,
    DaysConverter::class,
    DurationConverter::class,
)
abstract class ApplicationRoomDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDAO
    abstract fun dateDAO(): DateDAO
    companion object {
        val MIGRATION_7_8: Migration = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                val cursor = database.query("SELECT * FROM users LIMIT 0")
                val columnExists = cursor.getColumnIndex("darkMode") != -1
                cursor.close()

                if (!columnExists) {
                    database.execSQL("ALTER TABLE users ADD COLUMN darkMode INTEGER NOT NULL DEFAULT 0")
                }
            }
        }

        @Volatile
        private var INSTANCE: ApplicationRoomDatabase? = null
        fun getDatabase(context: Context): ApplicationRoomDatabase {
            // don't use isOpen because the value may be out dated
            if (INSTANCE == null) {
                synchronized(this) {
                    val reason = if (INSTANCE == null) "DB does not exist" else "DB is closed"
                    Log.d("RoomDatabase", "Constructing new database because $reason")
                    val path = context.getDatabasePath("user_database.db").path
                    val databaseFile = File(path)
                    val instance = Room.databaseBuilder(
                        context,
                        ApplicationRoomDatabase::class.java,
                        databaseFile.absolutePath,
                    )
                        .addMigrations(MIGRATION_7_8)
                        .addCallback(getCallback())
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                    Log.d("RoomDatabase", "Opening a new database $INSTANCE")
                    return instance
                }
            }
            Log.d("RoomDatabase", "Return existing database $INSTANCE")
            return INSTANCE as ApplicationRoomDatabase
        }

        fun closeDatabase() {
            Log.d("RoomDatabase", "Closing database instance $INSTANCE")
            if (INSTANCE != null) {
                if (INSTANCE!!.isOpen) {
                    INSTANCE!!.close()
                }
                INSTANCE = null
            }
        }

        fun getCallback(): Callback {
            return object : Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    db.execSQL("CREATE TEMP TABLE room_table_modification_log(table_id INTEGER PRIMARY KEY, invalidated INTEGER NOT NULL DEFAULT 0)")
                }
            }
        }
    }
}

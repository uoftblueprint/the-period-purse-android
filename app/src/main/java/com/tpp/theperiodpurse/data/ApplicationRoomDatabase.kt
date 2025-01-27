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
import com.tpp.theperiodpurse.data.dao.UserDAO
import com.tpp.theperiodpurse.data.entity.Date
import com.tpp.theperiodpurse.data.entity.User
import com.tpp.theperiodpurse.data.helper.DateConverter
import com.tpp.theperiodpurse.data.helper.DaysConverter
import com.tpp.theperiodpurse.data.helper.DurationConverter
import com.tpp.theperiodpurse.data.helper.SymptomConverter
import java.io.File
import javax.inject.Singleton


@Database(entities = [User::class, Date::class], version = 10, exportSchema = true)
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

        val MIGRATION_8_9: Migration = object : Migration(8, 9) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Check User table columns
                val userCursor = database.query("SELECT * FROM users LIMIT 0")
                val allowOvulationNotificationsExists = userCursor.getColumnIndex("allowOvulationNotifications") != -1
                val averageOvulationPhaseLengthExists = userCursor.getColumnIndex("averageOvulationPhaseLength") != -1
                val averageTimeBetweenPeriodAndOvulationExists = userCursor.getColumnIndex("averageTimeBetweenPeriodAndOvulation") != -1
                userCursor.close()

                // Check Date table column
                val dateCursor = database.query("SELECT * FROM dates LIMIT 0")
                val ovulatingExists = dateCursor.getColumnIndex("ovulating") != -1
                dateCursor.close()

                // Add columns if they don't exist
                if (!allowOvulationNotificationsExists) {
                    database.execSQL("ALTER TABLE users ADD COLUMN allowOvulationNotifications INTEGER NOT NULL DEFAULT 0")
                }
                if (!averageOvulationPhaseLengthExists) {
                    database.execSQL("ALTER TABLE users ADD COLUMN averageOvulationPhaseLength INTEGER NOT NULL DEFAULT 0")
                }
                if (!averageTimeBetweenPeriodAndOvulationExists) {
                    database.execSQL("ALTER TABLE users ADD COLUMN averageTimeBetweenPeriodAndOvulation INTEGER NOT NULL DEFAULT 0")
                }
                if (!ovulatingExists) {
                    database.execSQL("ALTER TABLE dates ADD COLUMN ovulating INTEGER DEFAULT 0")
                }
            }
        }

        val MIGRATION_9_10: Migration = object : Migration(9, 10) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Step 1: Add a new temporary column to store the new enum values
                database.execSQL("ALTER TABLE dates ADD COLUMN temp_ovulating TEXT")

                // Step 2: Populate the temporary column based on the current `ovulating` column
                database.execSQL("""
            UPDATE dates
            SET temp_ovulating = CASE
                WHEN ovulating = 1 THEN 'Ovulating'
                WHEN ovulating = 0 THEN NULL
            END
        """)

                // Step 3: Drop the old `ovulating` column
                database.execSQL("ALTER TABLE dates DROP COLUMN ovulating")

                // Step 4: Rename the temporary column to `ovulating`
                database.execSQL("ALTER TABLE dates RENAME COLUMN temp_ovulating TO ovulating")
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
                        .addMigrations(MIGRATION_7_8, MIGRATION_8_9, MIGRATION_9_10)
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

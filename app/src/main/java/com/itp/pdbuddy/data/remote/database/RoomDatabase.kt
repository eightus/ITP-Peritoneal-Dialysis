package com.itp.pdbuddy.data.remote.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.itp.pdbuddy.data.remote.NotificationDAO
import com.itp.pdbuddy.data.remote.table.Notification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.
 */
@Database(entities = [Notification::class], version = 1)
abstract class NotificationRoomDatabase : RoomDatabase() {

    //abstract fun Dao(): NotificationDAO
    abstract fun notificationDAO(): NotificationDAO

    companion object {
        @Volatile // ensure no cache
        private var INSTANCE: NotificationRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): NotificationRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotificationRoomDatabase::class.java,
                    "notification_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    .fallbackToDestructiveMigration()
                    .addCallback(NotificationDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class NotificationDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.notificationDAO())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        suspend fun populateDatabase(dao: NotificationDAO) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            dao.deleteAll()
        }
    }
}

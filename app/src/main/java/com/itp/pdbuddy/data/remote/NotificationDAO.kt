package com.itp.pdbuddy.data.remote

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.itp.pdbuddy.data.remote.table.Notification
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDAO {

    @Query("SELECT * FROM notification_table")
    fun getItems(): Flow<List<Notification>>

    @Query("SELECT id FROM notification_table WHERE time = :time AND date = :date AND type = :type")
    fun getId(time: String, date: String, type: String): List<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notification: Notification)

    @Query("DELETE FROM notification_table")
    suspend fun deleteAll()

    @Query("DELETE FROM notification_table WHERE id = :id")
    suspend fun deleteById(id: Int)
}
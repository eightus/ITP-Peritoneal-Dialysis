package com.itp.pdbuddy.data.remote.table;

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_table")
data class Notification(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "time") val time: String,
        @ColumnInfo(name = "date") val date: String,
        @ColumnInfo(name = "type") val type: String
)


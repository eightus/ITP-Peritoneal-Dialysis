package com.itp.pdbuddy.data.model;

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_table")
data class Notification(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "time") val time: String,
        @ColumnInfo(name = "date") val date: String,
        @ColumnInfo(name = "type") val type: String,
        @ColumnInfo(name = "medication") val medication: String,
        @ColumnInfo(name = "quantity") val quantity: String,
        @ColumnInfo(name = "unit") val unit: String,
)


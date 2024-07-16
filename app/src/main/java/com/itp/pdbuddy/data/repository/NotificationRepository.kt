package com.itp.pdbuddy.data.repository

import com.itp.pdbuddy.data.remote.NotificationDAO
import com.itp.pdbuddy.data.model.Notification
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class NotificationRepository @Inject constructor(
    private val dao: NotificationDAO
) {

    val allItems: Flow<List<Notification>> = dao.getItems()

    suspend fun insert(notification: Notification){
        dao.insert(notification)
    }

    suspend fun deleteNotificationById(id: Int) {
        dao.deleteById(id)
    }

    suspend fun delete() {
        dao.deleteAll()
    }

    suspend fun getId(time: String, date: String, type: String, medication: String): List<Int> {
        return dao.getId(time, date, type, medication)
    }


}
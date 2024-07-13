package com.itp.pdbuddy.ui.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itp.pdbuddy.data.model.Notification
import com.itp.pdbuddy.data.repository.NotificationRepository
import com.itp.pdbuddy.service.NotificationService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationRepository: NotificationRepository
): ViewModel() {

    val notificationService = NotificationService()
    val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)

    val allItems = notificationRepository.allItems.stateIn(
        scope = viewModelScope, // Coroutine scope
        started = SharingStarted.WhileSubscribed(), // When to start collecting
        initialValue = emptyList() // Initial value
    )

    fun createNotification(
        calenderState: MutableState<Calendar>,
        notificationType: String,
        medicine: String,
        quantity: String,
        unit: String
    ){
        val currentCalender = Calendar.getInstance()
        var date: String = "None"
        var time: String = "None"

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Notification Permission Not Granted, Enable to receive notification", Toast.LENGTH_LONG).show()
            return
        }

        // If medication (repeating notification), add one day to given value
        var message: String = "Notification"
        if (notificationType == "Medication"){
            // If before current time, add one day
            if (currentCalender.after(calenderState.value)){
                calenderState.value.add(Calendar.DAY_OF_YEAR, 1)
            }
            message = "It is time to take your medication: ${medicine} ${quantity}${unit}"
            date = "Recurring"
            time = SimpleDateFormat("hh:mm a").format(calenderState.value.time)
        }
        else if (notificationType == "Appointment"){
            val oriCalendarState = calenderState.value.clone() as Calendar
            calenderState.value.add(Calendar.DAY_OF_YEAR, -1)
            message = "You have a doctor appointment tomorrow at ${SimpleDateFormat("hh:mm a").format(calenderState.value.time)}"
            date = SimpleDateFormat("dd/MM/yyyy").format(oriCalendarState.time)
            time = SimpleDateFormat("hh:mm a").format(calenderState.value.time)
        }
        // Find time difference between now and notification time
        try {
            val setDate: Date = inputFormat.parse(calenderState.value.time.toString())!!
            val currentDate: Date = inputFormat.parse(currentCalender.time.toString())!!
            var different = setDate.time - currentDate.time
            Log.d(calenderState.value.time.toString(), different.toString())

            // Insert into room database
            viewModelScope.launch {
                notificationRepository.insert(
                    Notification(
                        date = date,
                        time = time,
                        type = notificationType,
                        medication = medicine,
                        quantity = quantity,
                        unit = unit
                    )
                )
                val id: List<Int>
                withContext(Dispatchers.IO){
                    id = notificationRepository.getId(time, date, notificationType, medicine)
                }
                Log.d(id.first().toString(), id.toString())
                if(notificationType == "Medication"){
                    notificationService.schedulePeriodicNotification(
                        context, different, "PDBuddy", message, workId = id.first().toString())
                }else if(notificationType == "Appointment"){
                    notificationService.scheduleOneTimeNotification(
                        context, different, "PDBuddy", message, workId = id.first().toString())
                }
            }


        } catch (e: ParseException) {
            Log.d(e.toString(), e.toString())
            return
        }
    }

    fun deleteNotification(id: Int){
        viewModelScope.launch {
            notificationRepository.deleteNotificationById(id)
            notificationService.removeWork(context, id)
        }
    }
}


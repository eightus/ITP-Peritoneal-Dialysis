package com.itp.pdbuddy.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.*
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.itp.pdbuddy.MainActivity
import com.itp.pdbuddy.R
import com.itp.pdbuddy.data.remote.NotificationDAO
import com.itp.pdbuddy.data.repository.NotificationRepository
import com.itp.pdbuddy.ui.viewmodel.NotificationViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import java.util.concurrent.TimeUnit

class NotificationService {

    fun scheduleOneTimeNotification(
        context: Context,
        delayInMillis: Long,
        title: String,
        message: String,
        type: String = "Appointment",
        workId: String) {
        val workManager = WorkManager.getInstance(context)

        val data = Data.Builder()
            .putString("title", title)
            .putString("message", message)
            .putString("workId", message)
            .putString("type", type)
            .build()

        val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        //workManager.enqueue("test",notificationWork)
        workManager.enqueueUniqueWork(workId, ExistingWorkPolicy.REPLACE, notificationWork)
        Toast.makeText(context, "Notification Created", Toast.LENGTH_LONG).show()
    }

    fun schedulePeriodicNotification(
        context: Context,
        delayInMillis: Long,
        title: String,
        message: String,
        type: String = "Appointment",
        workId: String) {
        val workManager = WorkManager.getInstance(context)



        val data = Data.Builder()
            .putString("title", title)
            .putString("message", message)
            .putString("workId", workId)
            .putString("type", type)
            .build()

        val notificationWork = PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        //workManager.enqueue("test",notificationWork)
        workManager.enqueueUniquePeriodicWork(workId, ExistingPeriodicWorkPolicy.REPLACE, notificationWork)
        Toast.makeText(context, "Notification Created", Toast.LENGTH_LONG).show()
    }

    fun removeWork(
        context: Context,
        workId: Int){
        val workManager = WorkManager.getInstance(context)
        try{
            workManager.cancelUniqueWork(workId.toString())
        }catch (e: Exception){
            Log.d(e.toString(), e.message.toString())
        }

    }
}

class NotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val title = inputData.getString("title") ?: "Notification Title"
        val message = inputData.getString("message") ?: "Notification Message"
        showNotification(applicationContext, title, message)
        return Result.success()
    }

    private fun showNotification(context: Context, title: String, message: String) {
        val builder = NotificationCompat.Builder(context, "PDBuddy")
            .setSmallIcon(R.drawable.splash_heart)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.d("No permission", "No permission")

                return
            }
            notify(1, builder.build())
        }
    }
}


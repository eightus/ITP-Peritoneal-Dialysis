package com.itp.pdbuddy.data.remote.firebase


import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FirebaseNotificationClient : FirebaseMessagingService()  {

    override fun onNewToken(token: String) {
        Log.d("FirebaseNotificationClient", "Refreshed token: $token")
        // Send the token to your server or save it locally
        sendRegistrationToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FirebaseNotificationClient", "From: ${remoteMessage.from}")

        // Handle data payload of FCM messages
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("FirebaseNotificationClient", "Message data payload: ${remoteMessage.data}")
            // Handle the data message here
        }

        // Handle notification payload of FCM messages
        remoteMessage.notification?.let {
            Log.d("FirebaseNotificationClient", "Message Notification Body: ${it.body}")
            // Handle the notification message here
        }

    }


    private fun sendRegistrationToServer(token: String) {
        // Implement this method to send token to your app server.
        // Example:
        Log.d("FirebaseNotificationClient", "Sending token to server: $token")
        // val serverUrl = "https://your.server.com/api/register-token"
        // val requestBody = RequestBody.create(MediaType.parse("application/json"), "{\"token\":\"$token\"}")
        // Use an HTTP client like Retrofit or OkHttp to send the token to your server.
    }

    // Retrieve the current token manually if needed
    fun retrieveCurrentToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FirebaseNotificationClient", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d("FirebaseNotificationClient", "FCM registration token: $token")
            sendRegistrationToServer(token)
        }
    }




}

package com.itp.pdbuddy.service
import android.content.Context
import android.net.Uri
import android.util.Base64
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject

class Base64Service @Inject constructor(
    @ApplicationContext private val context: Context // Inject the application context
) {
    suspend fun encodeImageToBase64(imageUri: Uri): String? {
        // Use the injected context here
        val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)

        inputStream?.let { stream ->
            val byteArrayOutputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var length: Int

            // Read input stream into ByteArrayOutputStream
            while (stream.read(buffer).also { length = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, length)
            }

            // Convert the ByteArray to Base64
            val byteArray = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.DEFAULT) // Return the Base64 string
        }
        return null // Return null if inputStream is null
    }
}
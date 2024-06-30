package com.itp.pdbuddy.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class Resource(
    val title: String = "",
    val description: String = "",
    val type: String = "",
    val content: String = "",
    val videoUrl: String? =  null,
    val websiteUrl: String? = null
)
class ResourcesRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getResources(): List<Resource> {
        return try {
            val snapshot = db.collection("Resources").get().await()
            snapshot.documents.map { document ->
                val resource = document.toObject<Resource>() ?: Resource()
                resource.copy(content = splitContent(resource.content).joinToString("\n"))
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun splitContent(content: String): List<String> {
        return content.split(". ").map { it.trim() + ". " }
    }
}
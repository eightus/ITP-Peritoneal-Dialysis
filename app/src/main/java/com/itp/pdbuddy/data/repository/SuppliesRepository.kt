package com.itp.pdbuddy.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.itp.pdbuddy.data.remote.AuthDataSource
import com.itp.pdbuddy.ui.screen.SupplyItem
import com.itp.pdbuddy.utils.Result
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SuppliesRepository @Inject constructor(
    /* private val authDataSource: AuthDataSource */
) {

    /*suspend fun getUsername(): Result<String?> {
        return authDataSource.getUsername()
    } */
    private val db = FirebaseFirestore.getInstance()

    private fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    suspend fun fetchSuppliesList(): List<String> {
        return try {
            val querySnapshot = db.collection("supplies").get().await()
            querySnapshot.documents.map { doc ->
                doc.getString("name") ?: ""
            }
        } catch (e: Exception) {
            // Handle any errors here
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun fetchUserSupplies(): List<SupplyItem> {
        val userId = getCurrentUserId()
        return if (userId != null) {
            try {
                val querySnapshot = db.collection("CurrentSupplies")
                    .whereEqualTo("userId", userId)
                    .get().await()

                querySnapshot.documents.mapNotNull { doc ->
                    val name = doc.getString("name")
                    val quantity = doc.getLong("quantity")?.toInt()
                    if (name != null && quantity != null) {
                        SupplyItem(name, quantity, checked = true, userId = userId)
                    } else {
                        null
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    fun updateSupplyQuantityInFirestore(item: SupplyItem, newQuantity: Int, onSuccess: () -> Unit) {
        val userId = getCurrentUserId()
        if (userId != null) {
            val query = db.collection("CurrentSupplies")
                .whereEqualTo("name", item.name)
                .whereEqualTo("userId", userId)

            query.get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        for (document in documents) {
                            document.reference.update("quantity", newQuantity)
                                .addOnSuccessListener {
                                    onSuccess()
                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Error updating document", e)
                                }
                        }
                    } else {
                        Log.w(TAG, "No matching documents found")
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error getting documents: ", e)
                }
        } else {
            Log.e(TAG, "User ID is null, cannot update supply quantity.")
        }
    }


    fun addSuppliesToFirestore(supplies: List<SupplyItem>) {
        val collectionRef = db.collection("CurrentSupplies")
        val userId = getCurrentUserId()

        supplies.forEach { supply ->
            if (supply.checked) {
                val itemData = hashMapOf(
                    "name" to supply.name,
                    "quantity" to supply.quantity,
                    "userId" to userId
                )

                collectionRef
                    .whereEqualTo("name", supply.name)
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            collectionRef.add(itemData)
                                .addOnSuccessListener { documentReference ->
                                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Error adding document", e)
                                }
                        } else {
                            Log.d(TAG, "Supply already exists in Firestore")
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error checking document", e)
                    }
            }
        }
    }

    fun deleteSupplyFromFirestore(supplyItem: SupplyItem, onSuccess: () -> Unit) {
        val userId = getCurrentUserId()

        if (userId != null) {
            db.collection("CurrentSupplies")
                .whereEqualTo("name", supplyItem.name)
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        document.reference.delete()
                            .addOnSuccessListener {
                                onSuccess()
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error deleting document", e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error getting documents: ", e)
                }
        } else {
            Log.e(TAG, "User ID is null, cannot delete supply item.")
        }
    }

}
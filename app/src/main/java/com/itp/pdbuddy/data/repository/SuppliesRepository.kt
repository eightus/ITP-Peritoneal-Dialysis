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

    private suspend fun getCurrentUsername(): String? {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        return if (userId != null) {
            try {
                val documentSnapshot = db.collection("users").document(userId).get().await()
                documentSnapshot.getString("username")
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            null
        }
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
        val username = getCurrentUsername()
        return if (username != null) {
            try {
                val querySnapshot = db.collection("CurrentSupplies")
                    .whereEqualTo("username", username)
                    .get().await()

                querySnapshot.documents.mapNotNull { doc ->
                    val name = doc.getString("name")
                    val quantity = doc.getLong("quantity")?.toInt()
                    //val imageUrl = doc.getString("imageUrl")
                    if (name != null && quantity != null ) {
                        SupplyItem(name, quantity, checked = true, userId = username)
                    } else {
                        null
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching user supplies: ${e.localizedMessage}")
                emptyList()
            }
        } else {
            Log.e(TAG, "Username is null, cannot fetch user supplies.")
            emptyList()
        }
    }

    suspend fun updateSupplyQuantityInFirestore(item: SupplyItem, newQuantity: Int, onSuccess: () -> Unit) {
        val username = getCurrentUsername()
        if (username != null) {
            val query = db.collection("CurrentSupplies")
                .whereEqualTo("name", item.name)
                .whereEqualTo("username", username)

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
            Log.e(TAG, "Username is null, cannot update supply quantity.")
        }
    }


    suspend fun addSuppliesToFirestore(supplies: List<SupplyItem>) {
        val collectionRef = db.collection("CurrentSupplies")
        val username = getCurrentUsername()

        supplies.forEach { supply ->
            if (supply.checked) {
                val itemData = hashMapOf(
                    "name" to supply.name,
                    "quantity" to supply.quantity,
                    "username" to username
                )

                try {
                    val documents = collectionRef
                        .whereEqualTo("name", supply.name)
                        .whereEqualTo("username", username)
                        .get()
                        .await()

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
                } catch (e: Exception) {
                    Log.e(TAG, "Error adding supplies to Firestore: ${e.localizedMessage}")
                }
            }
        }
    }

    suspend fun deleteSupplyFromFirestore(supplyItem: SupplyItem, onSuccess: () -> Unit) {
        val username = getCurrentUsername()

        if (username != null) {
            db.collection("CurrentSupplies")
                .whereEqualTo("name", supplyItem.name)
                .whereEqualTo("username", username)
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
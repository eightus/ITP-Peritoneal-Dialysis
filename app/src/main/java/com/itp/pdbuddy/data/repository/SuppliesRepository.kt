package com.itp.pdbuddy.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.itp.pdbuddy.ui.screen.SupplyItem
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SuppliesRepository @Inject constructor() {
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
                    if (name != null && quantity != null) {
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

    suspend fun fetchCartItems(): List<SupplyItem> {
        val username = getCurrentUsername()
        return if (username != null) {
            try {
                val querySnapshot = db.collection("CartSupplies")
                    .whereEqualTo("username", username)
                    .get().await()

                querySnapshot.documents.mapNotNull { doc ->
                    val name = doc.getString("name")
                    val quantity = doc.getLong("quantity")?.toInt()
                    if (name != null && quantity != null) {
                        SupplyItem(name, quantity, checked = true, userId = username)
                    } else {
                        null
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching cart items: ${e.localizedMessage}")
                emptyList()
            }
        } else {
            Log.e(TAG, "Username is null, cannot fetch cart items.")
            emptyList()
        }
    }

    suspend fun addSuppliesToFirestore(supplies: List<SupplyItem>) {
        val username = getCurrentUsername()
        if (username != null) {
            val batch = db.batch()
            supplies.forEach { supplyItem ->
                val supplyRef = db.collection("CurrentSupplies").document()
                val data = hashMapOf<String, Any>(
                    "name" to supplyItem.name,
                    "quantity" to supplyItem.quantity,
                    "username" to username
                    // Add other fields as needed
                )
                batch.set(supplyRef, data)
            }
            try {
                batch.commit().await()
                Log.d(TAG, "Supplies added to Firestore successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Error adding supplies to Firestore: ${e.localizedMessage}")
            }
        } else {
            Log.e(TAG, "Username is null, cannot add supplies to Firestore.")
        }
    }

    suspend fun updateSupplyQuantityInFirestore(
        item: SupplyItem,
        newQuantity: Int,
        onSuccess: () -> Unit
    ) {
        val username = getCurrentUsername()
        if (username != null) {
            val supplyRef = db.collection("CurrentSupplies")
                .whereEqualTo("name", item.name)
                .whereEqualTo("username", username)
                .get().await()

            if (!supplyRef.isEmpty) {
                val document = supplyRef.documents[0]
                try {
                    // Ensure newQuantity is not null and prepare data accordingly
                    val newQuantityData = hashMapOf<String, Any>(
                        "quantity" to newQuantity
                        // Add other fields as needed
                    )

                    document.reference.update(newQuantityData).await()
                    onSuccess.invoke()
                    Log.d(TAG, "Supply quantity updated successfully")
                } catch (e: Exception) {
                    Log.e(TAG, "Error updating supply quantity: ${e.localizedMessage}")
                }
            } else {
                Log.e(TAG, "Supply not found for update")
            }
        } else {
            Log.e(TAG, "Username is null, cannot update supply quantity.")
        }
    }

    suspend fun deleteSupplyFromFirestore(supplyItem: SupplyItem, onSuccess: () -> Unit) {
        val username = getCurrentUsername()
        if (username != null) {
            val supplyRef = db.collection("CurrentSupplies")
                .whereEqualTo("name", supplyItem.name)
                .whereEqualTo("username", username)
                .get().await()

            if (!supplyRef.isEmpty) {
                val document = supplyRef.documents[0]
                try {
                    document.reference.delete().await()
                    onSuccess.invoke()
                    Log.d(TAG, "Supply deleted successfully")
                } catch (e: Exception) {
                    Log.e(TAG, "Error deleting supply: ${e.localizedMessage}")
                }
            } else {
                Log.e(TAG, "Supply not found for deletion")
            }
        } else {
            Log.e(TAG, "Username is null, cannot delete supply.")
        }
    }

    suspend fun addToCart(supplyItem: SupplyItem) {
        val username = getCurrentUsername()
        if (username != null) {
            val data = hashMapOf(
                "name" to supplyItem.name,
                "quantity" to supplyItem.quantity,
                "username" to username
                // Add other fields as needed
            )
            try {
                db.collection("CartSupplies").add(data).await()
                Log.d(TAG, "Supply added to cart successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Error adding supply to cart: ${e.localizedMessage}")
            }
        } else {
            Log.e(TAG, "Username is null, cannot add supply to cart.")
        }
    }

    suspend fun removeFromCart(supplyItem: SupplyItem, onSuccess: () -> Unit) {
        val username = getCurrentUsername()
        if (username != null) {
            val supplyRef = db.collection("CartSupplies")
                .whereEqualTo("name", supplyItem.name)
                .whereEqualTo("username", username)
                .get().await()

            if (!supplyRef.isEmpty) {
                val document = supplyRef.documents[0]
                try {
                    document.reference.delete().await()
                    onSuccess.invoke()
                    Log.d(TAG, "Supply deleted from cart successfully")
                } catch (e: Exception) {
                    Log.e(TAG, "Error deleting supply from cart: ${e.localizedMessage}")
                }
            } else {
                Log.e(TAG, "Supply not found in cart for deletion")
            }
        } else {
            Log.e(TAG, "Username is null, cannot delete supply from cart.")
        }
    }
}

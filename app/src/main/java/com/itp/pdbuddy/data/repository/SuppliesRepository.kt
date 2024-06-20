package com.itp.pdbuddy.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.itp.pdbuddy.ui.screen.SupplyItem
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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

    suspend fun getItemPrice(name: String): Double {
        try {
            val querySnapshot = db.collection("supplies")
                .whereEqualTo("name", name)  // Query to find documents where "name" matches

            val snapshot = querySnapshot.get().await()

            if (!snapshot.isEmpty) {
                // Assuming there is only one document with the matching name
                val docSnapshot = snapshot.documents[0]
                val price = docSnapshot.getDouble("price") ?: 1.0 // Default to 1.0 if price is null
                val itemName = docSnapshot.getString("name") ?: "Unknown" // Retrieve the name field

                Log.d(TAG, "Retrieved price for $itemName: $price")
                return price
            } else {
                Log.w(TAG, "No document found in supplies collection with name $name")
                return 0.0 // Return 0.0 or handle differently for non-existent documents
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching price for item $name: ${e.message}")
            return 0.0 // Return 0.0 on error
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
                    val price = doc.getDouble("price")
                    val quantity = doc.getLong("quantity")?.toInt()
                    if (name != null && price != null && quantity != null) {
                        SupplyItem(name, quantity, checked = true, userId = username, price = price )
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
        val price = getItemPrice(supplyItem.name)

        if (username != null) {
            val totalPrice = supplyItem.quantity * price
            val data = hashMapOf(
                "name" to supplyItem.name,
                "quantity" to supplyItem.quantity,
                "username" to username,
                "price" to totalPrice
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


    suspend fun placeOrder(cartItems: List<SupplyItem>) {
        val username = getCurrentUsername()
        if (username != null) {
            val batch = db.batch()

            // Create an order document
            val orderDocRef = db.collection("orders").document()
            val timestamp = System.currentTimeMillis()

            // Convert timestamp to Date object
            val date = Date(timestamp)

            // Format Date object to desired string format
            val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
            val formattedDate = sdf.format(date)

            val orderData = hashMapOf(
                "orderId" to orderDocRef.id,
                "userId" to username,
                "timestamp" to timestamp,
                "formattedTimestamp" to formattedDate, // Add formatted timestamp
                "items" to cartItems.map { it.toOrderItemMap() },
                "totalAmount" to cartItems.sumOf { it.price }
            )
            batch.set(orderDocRef, orderData)

            // Update quantities in CurrentSupplies
            for (item in cartItems) {
                val currentSuppliesQuery = db.collection("CurrentSupplies")
                    .whereEqualTo("name", item.name)
                    .whereEqualTo("username", username)
                    .get().await()

                if (!currentSuppliesQuery.isEmpty) {
                    for (doc in currentSuppliesQuery.documents) {
                        val currentQuantity = doc.getLong("quantity")?.toInt() ?: 0
                        val newQuantity = currentQuantity + item.quantity

                        // Update the document with the new quantity
                        batch.update(doc.reference, "quantity", newQuantity)

                        // Break after the first match to avoid updating multiple documents with the same name
                        break
                    }
                } else {
                    // If the item doesn't exist in CurrentSupplies, add it with the new quantity
                    val newSupplyRef = db.collection("CurrentSupplies").document()
                    val newSupplyData = hashMapOf(
                        "name" to item.name,
                        "quantity" to item.quantity,
                        "username" to username
                        // Add other fields as needed
                    )
                    batch.set(newSupplyRef, newSupplyData)
                }
            }

            // Remove items from the cart
            for (item in cartItems) {
                val cartItemQuery = db.collection("CartSupplies")
                    .whereEqualTo("name", item.name)
                    .whereEqualTo("username", username)
                    .get().await()

                if (!cartItemQuery.isEmpty) {
                    for (doc in cartItemQuery.documents) {
                        batch.delete(doc.reference)
                    }
                } else {
                    Log.e(TAG, "Supply not found in CartSupplies for removal")
                }
            }

            try {
                batch.commit().await()
                Log.d(TAG, "Order placed and quantities updated successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Error placing order and updating quantities: ${e.localizedMessage}")
            }
        } else {
            Log.e(TAG, "Username is null, cannot place order.")
        }
    }




    fun SupplyItem.toOrderItemMap(): Map<String, Any> {
        return mapOf(
            "name" to name,
            "quantity" to quantity,
            "price" to price
            // Add other fields as needed
        )
    }
    suspend fun updateCartItemQuantity(supplyItem: SupplyItem, newQuantity: Int, onSuccess: () -> Unit) {
        val username = getCurrentUsername()
        if (username != null) {
            val cartItemQuery = db.collection("CartSupplies")
                .whereEqualTo("name", supplyItem.name)
                .whereEqualTo("username", username)
                .get().await()

            if (!cartItemQuery.isEmpty) {
                val document = cartItemQuery.documents[0]
                try {
                    val originalPrice = getItemPrice(supplyItem.name)
                    val newTotalPrice = newQuantity * originalPrice

                    val newQuantityData = mapOf(
                        "quantity" to newQuantity,
                        "price" to newTotalPrice
                    )

                    document.reference.update(newQuantityData).await()
                    onSuccess.invoke()
                } catch (e: Exception) {
                    Log.e(TAG, "Error updating cart item quantity: ${e.localizedMessage}")
                }
            } else {
                Log.e(TAG, "Cart item not found for update")
            }
        } else {
            Log.e(TAG, "Username is null, cannot update cart item quantity.")
        }
    }


}



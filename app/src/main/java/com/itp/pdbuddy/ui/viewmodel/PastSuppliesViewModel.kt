package com.itp.pdbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.itp.pdbuddy.data.repository.SuppliesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class Item(
    val name: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0
)

data class Order(
    val orderId: String = "",
    val timestamp: Long = 0,
    val formattedTimestamp: String = "",
    val totalAmount: Int = 0,
    val userId: String = "",
    val items: List<Item> = emptyList()
)

@HiltViewModel
class PastSuppliesViewModel @Inject constructor(private val suppliesRepository: SuppliesRepository) : ViewModel() {
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private val _orderDetails = MutableStateFlow<Order?>(null)
    val orderDetails: StateFlow<Order?> = _orderDetails

    private val db = FirebaseFirestore.getInstance()

    init {
        fetchOrders()
    }

    private fun fetchOrders() {
        viewModelScope.launch {
            db.collection("orders")
                .get()
                .addOnSuccessListener { result ->
                    val ordersList = result.map { document ->
                        val items = document.get("items") as List<Map<String, Any>>
                        val itemList = items.map {
                            Item(
                                name = it["name"] as String,
                                price = (it["price"] as Double).toDouble(),
                                quantity = (it["quantity"] as Long).toInt()
                            )
                        }
                        val timestamp = document.getLong("timestamp") ?: 0
                        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
                        val formattedTimestamp = sdf.format(Date(timestamp))

                        Order(
                            orderId = document.id,
                            timestamp = timestamp,
                            formattedTimestamp = formattedTimestamp, // Set the formatted timestamp
                            totalAmount = document.getLong("totalAmount")?.toInt() ?: 0,
                            userId = document.getString("userId") ?: "",
                            items = itemList
                        )
                    }
                    _orders.value = ordersList
                }
                .addOnFailureListener { exception ->
                    // Handle any errors here
                }
        }
    }

    fun fetchOrderDetails(orderId: String) {
        viewModelScope.launch {
            db.collection("orders").document(orderId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val items = document.get("items") as List<Map<String, Any>>
                        val itemList = items.map {
                            Item(
                                name = it["name"] as String,
                                price = (it["price"] as Double).toDouble(),
                                quantity = (it["quantity"] as Long).toInt()
                            )
                        }
                        val timestamp = document.getLong("timestamp") ?: 0
                        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
                        val formattedTimestamp = sdf.format(Date(timestamp))

                        val order = Order(
                            orderId = document.id,
                            timestamp = timestamp,
                            formattedTimestamp = formattedTimestamp, // Set the formatted timestamp
                            totalAmount = document.getLong("totalAmount")?.toInt() ?: 0,
                            userId = document.getString("userId") ?: "",
                            items = itemList
                        )
                        _orderDetails.value = order
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle any errors here
                }
        }
    }
}
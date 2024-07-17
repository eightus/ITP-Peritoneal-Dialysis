package com.itp.pdbuddy.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.itp.pdbuddy.ui.screen.Clinic
import com.itp.pdbuddy.ui.screen.SupplyRequest
import com.itp.pdbuddy.ui.viewmodel.TravelRequestViewModel.PastRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TravelRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getClinicsByCountry(country: String): List<Clinic> {
        return try {
            val snapshot = db.collection("Travel")
                .whereEqualTo("country", country)
                .get()
                .await()

            snapshot.documents.map { document ->
                Clinic(
                    clinicName = document.getString("clinicName") ?: "",
                    clinicAddress = document.getString("clinicAddress") ?: "",
                    clinicContactNumber = document.getString("clinicContactNumber") ?: ""
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getCountries(): List<String> {
        return try {
            val snapshot = db.collection("TravelCountry")
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                document.getString("country")
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun submitTravelRequest(
        country: String,
        hotelAddress: String,
        travelStartDate: String,
        travelEndDate: String,
        supplyRequests: List<SupplyRequest>,
        username: String?,
        totalPrice: Double,
        orderDate: String
    ) {
        val travelDates = "$travelStartDate - $travelEndDate"
        val request = hashMapOf(
            "country" to country,
            "hotelAddress" to hotelAddress,
            "travelDates" to travelDates,
            "username" to username,
            "totalPrice" to totalPrice,
            "orderDate" to orderDate,
            "supplies" to supplyRequests.map { supply ->
                mapOf(
                    "name" to supply.name,
                    "quantity" to supply.quantity,
                    "price" to supply.price
                )
            }
        )

        db.collection("TravelRequestForm")
            .add(request)
            .await()
    }

    suspend fun getSupplies(): List<SupplyRequest> {
        return try {
            val snapshot = db.collection("supplies")
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                SupplyRequest(
                    name = document.getString("name") ?: "",
                    price = document.getDouble("price") ?: 0.0
                )
            }
        } catch (e: Exception) {
            Log.e("TravelRepository", "Error fetching supplies", e)
            emptyList()
        }
    }

    suspend fun getPastRequests(username: String): List<PastRequest> {
        return try {
            val snapshot = db.collection("TravelRequestForm")
                .whereEqualTo("username", username)
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                val supplyRequests = (document.get("supplies") as List<Map<String, Any>>).map {
                    SupplyRequest(
                        name = it["name"] as String,
                        quantity = (it["quantity"] as Long).toInt(),
                        price = (it["price"] as? Double) ?: 0.0 // Default to 0.0 if price is null
                    )
                }

                PastRequest(
                    id = document.id,
                    orderDate = document.getString("orderDate") ?: "",
                    country = document.getString("country") ?: "",
                    hotelAddress = document.getString("hotelAddress") ?: "",
                    travelDates = document.getString("travelDates") ?: "",
                    totalPrice = document.getDouble("totalPrice") ?: 0.0, // Default to 0.0 if totalPrice is null
                    supplyRequests = supplyRequests
                )
            }
        } catch (e: Exception) {
            Log.e("TravelRepository", "Error fetching past requests", e)
            emptyList()
        }
    }

    suspend fun getCurrentUsername(): String? {
        val username = FirebaseAuth.getInstance().currentUser?.uid
        return if (username != null) {
            try {
                val documentSnapshot = db.collection("users").document(username).get().await()
                documentSnapshot.getString("username")
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            null
        }
    }

}
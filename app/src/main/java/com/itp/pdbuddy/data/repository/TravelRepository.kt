package com.itp.pdbuddy.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.itp.pdbuddy.ui.screen.Clinic
import com.itp.pdbuddy.ui.screen.SupplyRequest
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
        travelDates: String,
        supplyRequests: List<SupplyRequest>
    ) {
        val request = hashMapOf(
            "country" to country,
            "hotelAddress" to hotelAddress,
            "travelDates" to travelDates,
            "supplies" to supplyRequests.map { supply ->
                mapOf(
                    "name" to supply.name,
                    "quantity" to supply.quantity
                )
            }
        )

        db.collection("TravelRequestForm")
            .add(request)
            .await()
    }

    suspend fun getSupplies(): List<String> {
        return try {
            val snapshot = db.collection("supplies")
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                document.getString("name")
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

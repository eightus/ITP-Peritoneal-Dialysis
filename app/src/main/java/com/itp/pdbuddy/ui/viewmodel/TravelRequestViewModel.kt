package com.itp.pdbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itp.pdbuddy.data.repository.TravelRepository
import com.itp.pdbuddy.ui.screen.SupplyRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TravelRequestViewModel @Inject constructor(
    private val travelRepository: TravelRepository
) : ViewModel() {

    private val _supplies = MutableStateFlow<List<String>>(emptyList())
    val supplies: StateFlow<List<String>> get() = _supplies

    init {
        fetchSupplies()
    }

    private fun fetchSupplies() {
        viewModelScope.launch {
            val fetchedSupplies = travelRepository.getSupplies()
            _supplies.value = fetchedSupplies
        }
    }

    suspend fun submitTravelRequest(
        country: String,
        hotelAddress: String,
        travelDates: String,
        supplyRequests: List<SupplyRequest>
    ) {
        withContext(Dispatchers.IO) {
            travelRepository.submitTravelRequest(country, hotelAddress, travelDates, supplyRequests)
        }
    }
}

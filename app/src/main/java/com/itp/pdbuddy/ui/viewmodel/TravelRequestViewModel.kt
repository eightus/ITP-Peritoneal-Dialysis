package com.itp.pdbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itp.pdbuddy.data.repository.AuthRepository
import com.itp.pdbuddy.data.repository.TravelRepository
import com.itp.pdbuddy.ui.screen.SupplyRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.itp.pdbuddy.utils.Result

@HiltViewModel
class TravelRequestViewModel @Inject constructor(
    private val travelRepository: TravelRepository,
    private val authRepository: AuthRepository // Inject AuthRepository
) : ViewModel() {

    private val _supplies = MutableStateFlow<List<String>>(emptyList())
    val supplies: StateFlow<List<String>> get() = _supplies

    private val _username = MutableStateFlow<String?>(null)
    val username: StateFlow<String?> get() = _username

    init {
        fetchSupplies()
        fetchUsername() // Fetch the username on initialization
    }

    private fun fetchSupplies() {
        viewModelScope.launch {
            val fetchedSupplies = travelRepository.getSupplies()
            _supplies.value = fetchedSupplies
        }
    }

    private fun fetchUsername() {
        viewModelScope.launch {
            val result = authRepository.getUsername()
            if (result is Result.Success) {
                _username.value = result.data
            }
        }
    }

    suspend fun submitTravelRequest(
        country: String,
        hotelAddress: String,
        travelDates: String,
        supplyRequests: List<SupplyRequest>
    ) {
        val username = _username.value
        withContext(Dispatchers.IO) {
            travelRepository.submitTravelRequest(
                country = country,
                hotelAddress = hotelAddress,
                travelDates = travelDates,
                supplyRequests = supplyRequests,
                username = username
            )
        }
    }
}
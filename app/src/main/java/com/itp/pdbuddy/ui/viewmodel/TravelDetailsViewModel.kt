package com.itp.pdbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itp.pdbuddy.data.repository.TravelRepository
import com.itp.pdbuddy.ui.screen.Clinic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TravelDetailsViewModel @Inject constructor(private val travelRepository: TravelRepository) : ViewModel() {
    private val _clinics = MutableStateFlow<List<Clinic>>(emptyList())
    val clinics: StateFlow<List<Clinic>> = _clinics

    fun loadClinics(country: String) {
        viewModelScope.launch {
            val clinicList = travelRepository.getClinicsByCountry(country)
            _clinics.value = clinicList
        }
    }
}

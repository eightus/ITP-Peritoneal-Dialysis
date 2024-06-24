package com.itp.pdbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.itp.pdbuddy.data.repository.SuppliesRepository
import com.itp.pdbuddy.data.repository.TravelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TravelViewModel @Inject constructor(private val travelRepository: TravelRepository) : ViewModel() {
    suspend fun getCountries(): List<String> {
        return travelRepository.getCountries()
    }
}
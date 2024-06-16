package com.itp.pdbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.itp.pdbuddy.data.repository.SuppliesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PastSuppliesViewModel @Inject constructor(private val suppliesRepository: SuppliesRepository) : ViewModel() {
}
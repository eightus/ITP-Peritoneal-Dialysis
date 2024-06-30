package com.itp.pdbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itp.pdbuddy.data.model.Prescription
import com.itp.pdbuddy.data.repository.AuthRepository
import com.itp.pdbuddy.data.repository.PrescriptionRepository
import com.itp.pdbuddy.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class PrescriptionViewModel @Inject constructor(
    private val repository: PrescriptionRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _latestPrescription = MutableStateFlow<Result<Prescription>>(Result.Idle)
    val latestPrescription: StateFlow<Result<Prescription>> = _latestPrescription.asStateFlow()

    private val _allPrescriptions = MutableStateFlow<Result<List<Prescription>>>(Result.Idle)
    val allPrescriptions: StateFlow<Result<List<Prescription>>> = _allPrescriptions.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun fetchLatestPrescription() {
        _latestPrescription.value = Result.Loading
        viewModelScope.launch {
            when (val usernameResult = authRepository.getUsername()) {
                is Result.Success -> {
                    val username = usernameResult.data
                    if (username != null) {
                        val result = repository.getLatestPrescription(username)
                        _latestPrescription.value = result
                    } else {
                        _errorMessage.value = "Username is null"
                    }
                }
                is Result.Failure -> {
                    _errorMessage.value = usernameResult.exception.message
                }
                else -> {
                    _errorMessage.value = "Unknown error occurred"
                }
            }
        }
    }

    fun fetchAllPrescriptions() {
        viewModelScope.launch {
            when (val usernameResult = authRepository.getUsername()) {
                is Result.Success -> {
                    val username = usernameResult.data
                    if (username != null) {
                        _allPrescriptions.value = Result.Loading
                        val result = repository.getAllPrescriptions(username)
                        _allPrescriptions.value = result
                    } else {
                        _errorMessage.value = "Username is null"
                    }
                }
                is Result.Failure -> {
                    _errorMessage.value = usernameResult.exception.message
                }
                else -> {
                    _errorMessage.value = "Unknown error occurred"
                }
            }
        }
    }

    fun addPrescription(prescription: Prescription) {
        viewModelScope.launch {
            when (val usernameResult = authRepository.getUsername()) {
                is Result.Success -> {
                    val username = usernameResult.data
                    if (username != null) {
                        val updatedPrescription = prescription.copy(username = username)
                        repository.savePrescription(username, updatedPrescription)
                        fetchAllPrescriptions()
                    } else {
                        _errorMessage.value = "Username is null"
                    }
                }
                is Result.Failure -> {
                    _errorMessage.value = usernameResult.exception.message
                }
                else -> {
                    _errorMessage.value = "Unknown error occurred"
                }
            }
        }
    }

}
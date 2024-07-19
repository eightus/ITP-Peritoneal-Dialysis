package com.itp.pdbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itp.pdbuddy.data.model.Prescription
import com.itp.pdbuddy.data.repository.AuthRepository
import com.itp.pdbuddy.data.repository.RecordRepository
import com.itp.pdbuddy.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val repository: RecordRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    private val _recordResult = MutableStateFlow<Result<Boolean>>(Result.Idle)
    val recordResult: StateFlow<Result<Boolean>> = _recordResult.asStateFlow()

    private val _autoRecordResult = MutableStateFlow<Result<Boolean>>(Result.Idle)
    val autoRecordResult: StateFlow<Result<Boolean>> = _autoRecordResult.asStateFlow()

    private val _autoRecord = MutableStateFlow<List<Any>>(listOf())
    val autoRecord: StateFlow<List<Any>> = _autoRecord.asStateFlow()

    private val _latestPrescription = MutableStateFlow<Result<Prescription>>(Result.Idle)
    val latestPrescription: StateFlow<Result<Prescription>> = _latestPrescription.asStateFlow()


    fun submitRecord(name: String, data: List<Any>) {
        _recordResult.value = Result.Loading
        viewModelScope.launch {
            val result = repository.submitRecord(name, data)
            _recordResult.value = result
        }
    }

    fun getAutoRecord(){
        _autoRecordResult.value = Result.Loading
        viewModelScope.launch {
            delay(5000)
            when (val usernameResult = authRepository.getUsername()) {
                is Result.Success -> {
                    val username = usernameResult.data
                    if (username != null) {
                        val result = repository.getLatestPrescription(username)
                        _latestPrescription.value = result
                    } else {

                    }
                }
                is Result.Failure -> {

                }
                else -> {

                }
            }
            val autoRecord = repository.getAutoRecord()
            _autoRecordResult.value = Result.Success(true)
            _autoRecord.value = autoRecord
        }
    }
}
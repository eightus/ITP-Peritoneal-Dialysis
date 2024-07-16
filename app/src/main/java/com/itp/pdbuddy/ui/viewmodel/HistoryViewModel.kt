package com.itp.pdbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itp.pdbuddy.data.repository.AuthRepository
import com.itp.pdbuddy.data.repository.RecordRepository
import com.itp.pdbuddy.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val recordRepository: RecordRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _recordData = MutableStateFlow<Result<List<Map<String, Any>>>>(Result.Idle)
    val recordData: StateFlow<Result<List<Map<String, Any>>>> = _recordData.asStateFlow()

    fun getRecords() {
        viewModelScope.launch {
            try {
                val nameResult = authRepository.getUsername()
                if (nameResult is Result.Success) {
                    val name = nameResult.data
                    if (name != null) {
                        val allRecord = recordRepository.getRecords(name)
                        _recordData.value = allRecord
                    } else {
                        _recordData.value = Result.Failure(Exception("Username is null"))
                        Log.e("HistoryViewModel", "Username is null")
                    }
                } else {
                    _recordData.value = Result.Failure(Exception("Failed to get username"))
                    Log.e("HistoryViewModel", "Failed to get username")
                }
            } catch (e: Exception) {
                _recordData.value = Result.Failure(e)
                Log.e("HistoryViewModel", "Error fetching records", e)
            }
        }
    }
}

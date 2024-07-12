package com.itp.pdbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itp.pdbuddy.data.repository.AuthRepository
import com.itp.pdbuddy.data.repository.RecordRepository
import com.itp.pdbuddy.utils.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    private val recordRepository: RecordRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _recordData = MutableStateFlow<Result<String>>(Result.Idle)
    val recordData: StateFlow<Result<String>> = _recordData.asStateFlow()

    fun getRecords() {
        viewModelScope.launch {
            val nameResult = authRepository.getUsername()
            if (nameResult is Result.Success) {
                val name = nameResult.data
                if (name != null) {
                    val allRecord = recordRepository.getRecords(name)
                    // continue here
                } else {
                    TODO("H")
                    // fail here
                }
            }
        }
    }

}
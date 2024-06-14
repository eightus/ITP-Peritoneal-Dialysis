package com.itp.pdbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itp.pdbuddy.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.itp.pdbuddy.utils.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _sampleData = MutableStateFlow<Result<String>>(Result.Idle)
    val sampleData: StateFlow<Result<String>> = _sampleData.asStateFlow()


    fun doTest(sample: String) {
        viewModelScope.launch {
            _sampleData.value = Result.Loading
            delay(3000) // delay 3 second
            _sampleData.value = Result.Success(sample)
        }
    }
}
package com.itp.pdbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itp.pdbuddy.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import com.itp.pdbuddy.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel()  {
    private val _loginResult = MutableStateFlow<Result<Boolean>>(Result.Idle)
    val loginResult: StateFlow<Result<Boolean>> = _loginResult.asStateFlow()

    fun login(email: String, password: String) {
        _loginResult.value = Result.Loading
        viewModelScope.launch {
            val result = repository.login(email, password)
            _loginResult.value = result
        }
    }
}